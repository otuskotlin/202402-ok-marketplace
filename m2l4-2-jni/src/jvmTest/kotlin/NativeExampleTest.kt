package ru.otus.otuskotlin.interop

import kotlin.test.Test
import kotlin.test.assertEquals

class NativeExampleTest {
    @Test
    fun rustExampleTest() {
        val re = NativeExample()
        assertEquals(5, re.native_add(3, 2))
    }
}
