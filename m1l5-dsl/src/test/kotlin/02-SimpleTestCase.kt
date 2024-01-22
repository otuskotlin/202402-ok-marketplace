package ru.otus.otuskotlin.m1l5

import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleTestCase {
    @Test
    fun `minimal test`() {
        val s = sout {
            1 + 123
        }

        assertEquals("string 124", s)
    }

    private fun sout(block: () -> Int?): String {
        val result = block()
        println(result)
        return "string $result"
    }

    @Test
    fun `sout with prefix`() {
        soutWithContext {
            "${time()}: my line."
        }
    }

    class MyContext {
        fun time() = System.currentTimeMillis()
//    // Same as:
//    fun time(): Long {
//        return System.currentTimeMillis()
//    }
    }

    private fun soutWithContext(block: MyContext.() -> Any?) {
        val context = MyContext()
        val result = block(context)
        println(result)
    }


    @Test
    fun `dsl functions`() {
        val (key, value) = Pair("key", "value")
        assertEquals(key, "key")
        assertEquals(value, "value")

        val pairNew = "key" to "value"
        assertEquals(pairNew.first, "key")
        assertEquals(pairNew.second, "value")

        val myTimeOld = "12".time("30")
        assertEquals(myTimeOld, "12:30")

        val myTime = "12" time "30"
        assertEquals(myTime, "12:30")
    }

    private infix fun String.time(value: String): String {
        return "$this:$value"
    }
}
