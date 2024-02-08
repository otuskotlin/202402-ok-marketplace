import kotlin.test.Test
import kotlin.test.assertEquals

class InfixTest {
    @Test
    fun test() {
        assertEquals("str: n is 5", "str" myInfix 5)
    }
    private infix fun String.myInfix(n: Int) = "$this: n is $n"
}
