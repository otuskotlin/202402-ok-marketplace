import junit.framework.TestCase.assertFalse
import kotlin.test.Test
import kotlin.test.assertEquals

class IteratorTest {
    private val list = mutableListOf("string", "1", "2")

    @Test
    fun immutable() {
        val iter: Iterator<String> = list.iterator()
        // iter.remove() // Not allowed
        assertEquals("string", iter.next())
    }

    @Test
    fun mutable() {
        val mutIter: MutableIterator<String> = list.listIterator()
        mutIter.next()
        mutIter.remove()
        assertEquals("1", mutIter.next())
        assertFalse(list.contains("string"))
    }
}
