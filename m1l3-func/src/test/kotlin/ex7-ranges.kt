import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RangesTest {

    @Test
    fun inclusive() {
        val range: IntRange = (1..5)
        assertContains(range, 1)
        assertContains(range, 5)
    }

    @Test
    fun exclusive() {
        val prog: LongProgression = (1L until 5L)
        assertContains(prog, 1L)
        assertFalse(prog.contains(5))
    }

    @Test
    fun exclusive1() {
        val prog: LongProgression = (1L..<5L)
        assertContains(prog, 1L)
        assertFalse(prog.contains(5))
    }

    @Test
    fun countDown() {
        val prog: IntProgression = (5 downTo 1)
        assertEquals(5, prog.first())
        assertEquals(1, prog.last())
    }

    @Test
    fun step() {
        val prog: IntProgression = (1..5 step 2)
        assertTrue(prog.contains(1))
        assertFalse(prog.contains(2))
    }

    @Test
    fun iterate() {
        for(i in 1..3) {
            assertTrue(i in (1..3))
        }
    }

    @Test
    fun iterate1() {
        (1..8 step 2). forEach {
            assertTrue(it in (1..8))
        }
    }
}
