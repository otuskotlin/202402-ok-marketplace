package ru.otus.otuskotlin.coroutines

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.time.measureTime

class Ex7Async {

    @Test
    fun async1(): Unit = runBlocking {// ***
        val res1 = async {
            delay(1000)
            println("async1")
            42
        }
        val res2 = async {
            delay(500)
            println("async2")
            42
        }

        println("completed: ${res1.await()}, ${res2.await()}")
    }

    @Test
    fun asyncLazy(): Unit = runBlocking {// ***
        val res1 = async(start = CoroutineStart.LAZY) {
            println("async1 stated")
            delay(1000)
            println("async1")
            42
        }
        val res2 = async(start = CoroutineStart.LAZY) {
            println("async2 stated")
            delay(500)
            println("async2")
            66
        }

//        res1.start()
//        res2.start()
        delay(1000)
        println("we are here")
        val dur = measureTime {
            println("completed: ${res1.await()}, ${res2.await()}")
        }
        println("Duration: $dur")
    }
}
