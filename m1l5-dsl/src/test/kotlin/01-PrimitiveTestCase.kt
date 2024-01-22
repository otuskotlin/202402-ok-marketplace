package ru.otus.otuskotlin.m1l5

import kotlin.test.Test
import kotlin.test.assertEquals

class PrimitiveTestCase {
    @Test
    fun builderLessTest() {
        class SomeTest(
            val x: Int = 0,
            val s: String = "string $x",
        )
        val inst = SomeTest(5)
        assertEquals("string 5", inst.s)
    }
}
