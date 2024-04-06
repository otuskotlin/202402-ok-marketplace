@file:Suppress("UNUSED_PARAMETER")
package ru.otus.otuskotlin.coroutines

import kotlinx.coroutines.*
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class Ex3Coroutines {
    private suspend fun someMethod(): String { // ***
        println("Some method")
        delay(1000) // ***
        return "Some data"
    }

    private suspend fun otherMethod(a: Int, throwPlace: Int = 0): Int {
        if (throwPlace == 1) throw RuntimeException("1")
        println("Other method")
        if (throwPlace == 2) throw RuntimeException("2")
        delay(1000)
        return a * 2
    }

    @Suppress("unused")
    fun x() {
//        someMethod()
    }

    @Test
    fun coro(): Unit = runBlocking {// ***
        launch {// ***
            try {
                val str = someMethod()
                println("Apply")
                val len = str.length

                val num = otherMethod(len)
                println("Complete $num")
            } catch (ex: Exception) {
                println("Exception $ex")
            }
        }.join()
        println("Complete")
    }

    fun doSomething(block: suspend (Int) -> Unit) {
    }

    fun simpleMethod(i: Int) {
    }

    @Test
    fun signature(): Unit = runBlocking {
        val list = listOf(1, 2, 3)

        list.forEach { otherMethod(it) } // это работает, потому что inline
//        list.forEach (Consumer { otherMethod(it) }) // это не работает, потому что ждут обычную функцию

        doSomething(::otherMethod) // ждут suspend и передаем его
        doSomething(::simpleMethod) // ждут suspend, передаем обычный метод - это ок, котлин вставит преобразование
    }

    @Test
    fun launch2(): Unit = runBlocking {// ***
        launch {// ***
            for (i in 1..10) {
                println("coro1 $i")
                delay(100)
            }
        }

        launch {// ***
            for (i in 1..10) {
                println("coro2 $i")
                delay(110)
            }
        }
    }

    @Test
    fun launchMany(): Unit = runBlocking {// ***
        val counter = AtomicInteger()
        var dummyCounter = 0

        println("START")
        @OptIn(DelicateCoroutinesApi::class)
        newFixedThreadPoolContext(10, "fixed").use { fixThreadDispatcher ->
            launch {
                for (i in 0..1_000_000) {
                    launch(fixThreadDispatcher) {
                        delay(100)
                        counter.incrementAndGet()
                        dummyCounter++
                    }
                }
            }.join()
        }

        println("COMPLETE ${counter.get()}  Dummy: $dummyCounter")
    }

}
