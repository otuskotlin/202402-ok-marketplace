package ru.otus.otuskotlin.oop

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

enum class HighLowEnum {
    LOW,
    HIGH
}

enum class HighLowWithData(val level: Int, val description: String) {
    LOW(10, "low level"),
    HIGH(20, "high level")
}


enum class FooBarEnum : Iterable<FooBarEnum> {
    FOO {
        override fun doSmth() {
            println("do foo")
        }
    },

    BAR {
        override fun doSmth() {
            println("do bar")
        }
    };

    abstract fun doSmth()

    override fun iterator(): Iterator<FooBarEnum> = listOf(FOO, BAR).iterator()
}

class EnumsTest {
    @Test
    fun enum() {
        var e = HighLowEnum.LOW
        println(e)

        e = HighLowEnum.valueOf("HIGH")
        println(e)
        println(e.ordinal)
        assertEquals(1, e.ordinal)

        println(HighLowEnum.entries.joinToString())
    }

    @Test
    fun enumWithData() {
        var e = HighLowWithData.LOW
        println(e)

        e = HighLowWithData.valueOf("HIGH")
        println(e)
        println(e.ordinal)
        assertEquals(1, e.ordinal)
        assertEquals(20, e.level)

        println(HighLowEnum.entries.joinToString())
    }

    @Test
    fun interfacedEnums() {
        assertEquals(listOf(FooBarEnum.FOO, FooBarEnum.BAR), FooBarEnum.BAR.iterator().asSequence().toList())
        assertEquals(listOf(FooBarEnum.FOO, FooBarEnum.BAR), FooBarEnum.FOO.iterator().asSequence().toList())
    }

    @Test
    fun enumFailures() {
        assertFails {
            // Здесь будет исключение в рантайме
            HighLowEnum.valueOf("high")
        }

        val res = runCatching { HighLowEnum.valueOf("high") }
            .getOrDefault(HighLowEnum.HIGH)

        assertEquals(HighLowEnum.HIGH, res)
    }
}
