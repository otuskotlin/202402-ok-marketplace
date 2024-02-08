package ru.otus.otuskotlin.oop

import org.junit.Test
import kotlin.test.assertEquals

sealed interface Base

data object ChildA : Base

class ChildB : Base {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }
}

// Uncomment this to get compilation error
//class ChildC : Base

class SealedTest {
    @Test
    fun test() {
        val obj: Base = ChildA

        val result = when (obj) {
            is ChildA -> "a"
            is ChildB -> "b"
        }

        println(result)
        assertEquals(result, "a")
    }
}
