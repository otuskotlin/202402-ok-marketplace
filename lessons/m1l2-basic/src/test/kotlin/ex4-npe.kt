import org.junit.Assert.assertThrows
import org.junit.Test

private var x = 1

class NpeTest {
    @Test
    fun safety() {
        // val error1: String = null // ERROR
        var ok: String? = null
        when (x) {
            1 -> "Hello"
        }

        // println(ok.length) // ERROR

        // val notNull: String = ok // ERROR
        val mayBeNull: String? = ok

        if (ok != null)
            println(ok.length)

        ok = "hello"
        println(ok.length)
    }

    private fun someCheck(v: String?) = v != null

    @Test
    fun operators() {
        var ok: String? = null

        println(ok?.length?.toLong())

        println(ok?.length ?: -1)

        assertThrows(NullPointerException::class.java) {
            println(ok!!.length) // RUNTIME ERROR
        }

        if (someCheck(ok))
            println(ok!!.length)
    }

    @Test
    fun check() {
        var nullable = null
        val whatTypeIAm = nullable?.toDouble()
        val guessType = whatTypeIAm ?: 4.5
        val guessType2 = nullable?.toLong() ?: 5
        val guessType3 = guessType?.toInt() ?: 2
    }
}
