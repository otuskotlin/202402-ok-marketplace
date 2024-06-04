import kotlin.test.Test

class OnnxInferTest {
    @Test
    fun onnxinferTest() {
        val inferrer = Inferrer(
            modelPath = "onnx-model/model.onnx",
            tokenizerJson = "onnx-model/tokenizer.json",
        )
        inputTexts.forEach {
            println("========================================")
            println("TEXT: $it")
            inferrer.infer(it)
        }
    }

    companion object {
        val inputTexts = listOf(
            "Ahwar wants to work at Google in london. EU rejected German call to boycott British lamb.",
            """
KotlinDL is a high-level Deep Learning API written in Kotlin and inspired by Keras. Under the hood, it uses TensorFlow Java API and ONNX Runtime API for Java. KotlinDL offers simple APIs for training deep learning models from scratch, importing existing Keras and ONNX models for inference, and leveraging transfer learning for tailoring existing pre-trained models to your tasks.
This project aims to make Deep Learning easier for JVM and Android developers and simplify deploying deep learning models in production environments.
Here's an example of what a classic convolutional neural network LeNet would look like in KotlinDL:

        """.trimIndent(),
            """
«Я́ндекс» — российская транснациональная компания в отрасли информационных технологий, чьё головное юридическое лицо зарегистрировано в Нидерландах, владеющая одноимённой системой поиска в интернете, интернет-порталом и веб-службами в нескольких странах. Наиболее заметное положение занимает на рынках России, Белоруссии и Казахстана[5].

Поисковая система Yandex.ru была официально анонсирована 23 сентября 1997 года и первое время развивалась в рамках компании CompTek International. Как отдельная компания «Яндекс» образовалась в 2000 году.

В мае 2011 года «Яндекс» провёл первичное размещение акций, заработав на этом больше, чем какая-либо из интернет-компаний со времён IPO-поисковика Google в 2004 году[6][7].
        """.trimIndent(),
        )
    }
}
