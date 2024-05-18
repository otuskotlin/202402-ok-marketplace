import kotlin.test.Test
import kotlin.test.assertEquals

class Ex2_memory_managementKtTest {

    @Test
    fun sumStruct() {
        assertEquals(5.0, sumStruct(2, 3.0))
    }

    @Test
    fun sumRef() {
        assertEquals(5.0, sumRef(2, 3.0))
    }

    @Test
    fun sumAlloc() {
        assertEquals(5.0, sumAlloc(2, 3.0))
    }

    @Test
    fun sumCallback() {
        assertEquals(5.0, sumCallback(2, 3.0))
    }
}
