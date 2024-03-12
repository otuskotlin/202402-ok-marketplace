import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class GenericTest {

    @Test
    fun genericTest() {
        assertEquals("String", variant2<String>())
    }

    @Test
    fun elementAsListTest() {
        assertContains(elementAsList(12), 12)
    }


    /*
    fun <T> willNotCompile(variable: T) {
        println(T::class.java)
    }
     */

    private fun variant1(klass: KClass<*>): String = klass.simpleName ?: "(unknown)"
    private inline fun <reified T> variant2() = variant1(T::class)

    @Suppress("SameParameterValue")
    private fun <T> elementAsList(el: T): List<T> = listOf(el)

}
