package ru.otus.otuskotlin.interop

import kotlin.test.Test
import kotlin.test.assertEquals

class RustExampleTest {
    @Test
    fun rustExampleTest() {
        val re = RustExample()
        assertEquals(5, re.rust_add(3, 2))
    }
}
