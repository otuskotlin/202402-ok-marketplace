package ru.otus.otuskotlin.flows

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlin.test.Test

/**
 * Демонстрация квазипараллельной работы flow и корутин по сравнению с последовательностями.
 * Обратите внимание на скорость выполнения тестов
 * Dispatchers.IO.limitedParallelism(1) обеспечивает штатное и корректное выделение ровного одного потока для
 * корутин-контекста.
 */
class FlowVsSequenceTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun sequenceTest(): Unit = runBlocking(Dispatchers.IO.limitedParallelism(1)) {
        val simpleSequence = sequence {
            for (i in 1..5) {
//              delay(1000) // can't use it here
//              Thread.sleep блокирует корутину
                Thread.sleep(1000)
                yield(i)
            }
        }
        launch {
            for (k in 1..5) {
                println("I'm not blocked $k")
                delay(1000)
            }
        }
        simpleSequence.forEach { println(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun flowTest(): Unit = runBlocking(Dispatchers.IO.limitedParallelism(1)) {
        val simpleFlow = flow {
            for (i in 1..5) {
                delay(1000)
                emit(i)
            }
        }
        launch {
            for (k in 1..5) {
                println("I'm not blocked $k")
                delay(1000)
            }
        }
        simpleFlow.collect { println(it) }

        println("Flow end")
    }
}
