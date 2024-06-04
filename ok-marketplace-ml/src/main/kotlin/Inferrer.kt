import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtException
import ai.onnxruntime.OrtSession
import ai.onnxruntime.OrtSession.SessionOptions
import java.io.IOException
import java.nio.file.Paths

class Inferrer(
    modelPath: String = "model.onnx",
    private val tokenizerJson: String = "tokenizer.json",
) {
    private val tokenizer: HuggingFaceTokenizer by lazy {
        runCatching { HuggingFaceTokenizer.newInstance(Paths.get(tokenizerJson)) }
            .onFailure { e -> e.printStackTrace() }
            .getOrThrow()
    }
    private val env: OrtEnvironment by lazy {
        OrtEnvironment.getEnvironment() ?: throw Exception("Failed to get ORT environment")
    }
    private val session: OrtSession by lazy {
        val s = env.createSession(modelPath, SessionOptions()) ?: throw Exception("Failed to get session")
        println(
            """
                Model Input Names:  ${s.inputNames.joinToString()}
                Model Input info:   ${s.inputInfo.entries.joinToString { "${it.key}=${it.value}" }}
                Model Output Names: ${s.outputNames.joinToString()}
                Model Output info:  ${s.outputInfo.entries.joinToString { "${it.key}=${it.value}" }}
            """.trimIndent()
        )
        s
    }

    /*
        seperates tokens into arrays according to class ids

        below is the relation from class id to the label
        "id2label": {
        "0": "B-LOC",
        "1": "B-MISC",
        "2": "B-ORG",
        "3": "I-LOC",
        "4": "I-MISC",
        "5": "I-ORG",
        "6": "I-PER",
        "7": "O"
    * */
    private fun InferringResult.post(
        clazz: Int,
        token: String,
    ) = when (clazz) {
        6 -> persons += token
        2, 5 -> organizations += token
        3, 0 -> locations += token
        1, 4 -> misc += token
        else -> Unit
    }

    private fun findMaxIndex(arr: FloatArray): Int = arr.indices.maxBy { arr[it] }

    fun infer(inputText: String) = try {

        val encoding = try {
            tokenizer.encode(inputText)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            throw ioException
        }

        val tokens = encoding.tokens ?: throw Exception("No tokens detected") // tokenize the text
        val modelInputs = mapOf(
            "input_ids" to OnnxTensor.createTensor(
                env,
                arrayOf(encoding.ids ?: throw Exception("Empty ids"))
            ),
            "attention_mask" to OnnxTensor.createTensor(
                env,
                arrayOf(encoding.attentionMask ?: throw Exception("Empty attention mask"))
            ),
        )

        val inferringResult = InferringResult()
        @Suppress("UNCHECKED_CAST")
        session.run(modelInputs)
            ?.firstOrNull()
            ?.value
            ?.value
            ?.let { it as? Array<Array<FloatArray>> }
            ?.firstOrNull()
            ?.forEachIndexed { i, logits0i ->
                try {
                    inferringResult.post(findMaxIndex(logits0i), tokens[i])
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
            ?: throw Exception("Empty result")
        inferringResult.displayResult(tokens)
    } catch (e: OrtException) {
        e.printStackTrace()
    }

    private fun InferringResult.displayResult(tokens: Array<String>) {
        /*
        * Showing the results
        * */
        val tokensSpecialChar = tokens[1][0].toString() // word seperators in tokens
        println("All persons in the text: ${persons.cleanResult(tokensSpecialChar)}")
        println("All Organizations in the text: ${organizations.cleanResult(tokensSpecialChar)}")
        println("All Locations in the text: ${locations.cleanResult(tokensSpecialChar)}")
        println("All Miscellanous entities in the text: ${misc.cleanResult(tokensSpecialChar)}")
    }

    private fun String.cleanResult(tokensSpecialChar: String) = split(tokensSpecialChar.toRegex())
        .dropLastWhile { it.isEmpty() }
        .filter { it.isNotBlank() }
        .joinToString()
}
