import org.junit.Test
import kotlin.test.assertEquals

class StringsTest {
    @Test
    fun strings() {
        val string = "Hello, otus!\n"
        val elementOfString: Char = string[1] // string.get(1) 'e'
        assertEquals('e', elementOfString)

        val codeExample = """
            val string = "Hello, otus!\n"
            val elementOfString: Char = string[1] // string.get(1) 'e'
        """
        println("====\n" + codeExample + "\n====")

        val codeExample2 = """
            val string = "Hello, otus!\n"
            val elementOfString: Char = string[1] // string.get(1) 'e'
        """.trimIndent()
        println(codeExample2)
    }

    fun f(): String {
        for (i in 1 .. 3)
            println(i)
        return "hello"
    }

    @Test
    fun templates() {
        val a = 1
        val b = 2
        val string = "$a + $b = ${a+b}"
        assertEquals("1 + 2 = 3", string)

        assertEquals("${"hello, $a"}", "hello, 1")

        // не стоит увлекаться :)
        val c = "${
            when(a) {
                1 -> f()
                else -> "world"
            }
        }"
        assertEquals(c, "hello")
    }
}
