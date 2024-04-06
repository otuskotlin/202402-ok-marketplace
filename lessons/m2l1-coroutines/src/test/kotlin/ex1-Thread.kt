package ru.otus.otuskotlin.coroutines

import kotlin.concurrent.thread
import kotlin.test.Test

class Ex1Thread {
    @Test
    fun thr() {
        thread {
            println("Hello, thread started")
            try {
                for (i in 1..10) {
                    println("i = $i")
                    Thread.sleep(100)
                    if (i == 6) throw RuntimeException("Some error")
                }
            } catch (e: Exception) {
                println("Exception")
            }
        }.join()
        println("Thread complete")
    }
}
