package ru.otus.otuskotlin.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

/**
 * How to decompile file in IDEA
 *
 * 1. Select class: build -> classes -> kotlin -> main -> continuation -> DecompileMePleaseKt.class
 * 2. Decompile: Tools -> Kotlin -> Decompile to Java
 */

class Ex4Decompile {
    private suspend fun someMethod(): String {
        var a = 1
        var b = "x"
        println("1 $a $b")
        delay(100)

        a += otherMethod(5)
        b += "y"
        println("2 $a $b")

        delay(200)

        a += 3
        b = withCatch(b)
        println("3 $a $b")

        return "Some data $a $b"
    }

    // можете самостоятельно посмотреть во что декомпилируются остальные функции

    private suspend fun otherMethod(a: Int): Int {
        if (a == 0) return -1

        var b = a
        for (i in 1..10) {
            delay(50)
            b += 2
        }
        return b
    }

    private suspend fun withCatch(a: String): String {
        try {
            delay(100)
        } catch(e: Exception) {
            return "ex"
        }
        return "$a ok"
    }

    @Test
    fun coro(): Unit = runBlocking {// ***
        someMethod()
    }
}
