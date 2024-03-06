import org.junit.Assert.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.fail

private var x = 1

class ExceptionsTest {
    @Test
    fun throwJvmTest() {
        assertThrows(Exception::class.java) {
            throw Exception("my exception")
        }
    }
    @Test
    fun throwKotlinTest() {
        assertFails {
            throw Exception("my exception")
        }
    }

    @Test
    fun caughtTest() {
        try {
            throw Exception("caughtTest")
        } catch (e: RuntimeException) {
            fail("This must be not a Runtime Exception")
        } catch (e: Throwable) {
            println("Success")
        } finally {
            println("Final")
        }
    }

    @Test
    fun caughtExpressionTest() {
        val x = try {
            throw Exception("caughtTest")
        } catch (e: RuntimeException) {
            fail("This must be not a Runtime Exception")
        } catch (e: Throwable) {
            12
        } finally {
            println("Final")
        }
        assertEquals(12, x)
    }

    /*
    * В некоторых случаях можно избежать завала приложения по ошибке.
    * В этом примере мы спасаем приложение от Java heap space ошибки
    * */
    @Test
    fun memoryOverflowTest() {
        val x = try {
            LongArray(2_000_000_000) {
                it.toLong()
            }
        } catch (e: Throwable) {
            longArrayOf(1)
        }
        assertEquals(1, x.size)
    }
}
