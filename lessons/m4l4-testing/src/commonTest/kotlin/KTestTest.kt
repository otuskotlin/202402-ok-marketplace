import kotlin.test.*

class KTestTest {

    @Test
    fun kTest() {
        assertEquals(4, 2 * 2)
    }

    @Ignore
    @Test
    fun ignoredTest() {
        println("I will never be invoked")
    }

    @BeforeTest
    fun beforeTest() {
        println("Before Test")
    }

    @AfterTest
    fun afterTest() {
        println("After Test")
    }
}
