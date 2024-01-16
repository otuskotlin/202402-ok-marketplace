import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class CollectionsTest {
    private val list = mutableListOf("one", "one", "two")
    private val set = mutableSetOf("one", "one", "two")
    private val map = mapOf(
        "one" to "1a",
        "one" to "1",
        "two" to "2",
        "three" to "3",
    )

    @Test
    fun list() {
        assertEquals(listOf("one", "one", "two"), list)
        assertContains(list, "one")
    }

    @Test
    fun set() {
        assertEquals(setOf("one", "two"), set)
        assertContains(set, "one")
    }

    @Test
    fun map() {
        assertEquals(setOf("one", "two", "three"), map.keys)
        assertContains(map, "two")
    }

}
