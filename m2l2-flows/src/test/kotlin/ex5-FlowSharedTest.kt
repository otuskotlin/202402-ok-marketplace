package ru.otus.otuskotlin.flows

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.test.Test

class FlowSharedTest {

    /**
     * Пример работы SharedFlow
     */
    @Test
    fun shared(): Unit = runBlocking {
        val shFlow = MutableSharedFlow<String>()
        launch { shFlow.collect { println("XX $it") } } // Подписчики сами никогда не завершатся
        launch { shFlow.collect { println("YY $it") } }
        (1..10).forEach {
            delay(100)
            shFlow.emit("number $it")
        }
        coroutineContext.cancelChildren() // Таким образом мы закроем подписчиков
    }

    /**
     * Корректное разнесение функция публикации и получения
     */
    @Test
    fun collector(): Unit = runBlocking {
        val mshFlow = MutableSharedFlow<String>()
        val shFlow = mshFlow.asSharedFlow() // Только для подписчиков
        val collector: FlowCollector<String> = mshFlow // Только для публикации
        launch {
            mshFlow.collect {
                println("MUT $it")
            }
        }
        launch {
            shFlow.collect {
                println("IMMUT $it")
            }
        }
        delay(100)
        (1..20).forEach {
            collector.emit("zz: $it")
        }
        delay(1000)
        coroutineContext.cancelChildren()
    }

    /**
     * Пример конвертации Flow в SharedFlow
     */
    @Test
    fun otherShared(): Unit = runBlocking {
        val coldFlow = flowOf(100, 101, 102, 103, 104, 105).onEach { println("Cold: $it") }

        launch { coldFlow.collect() }
        launch { coldFlow.collect() }

        val hotFlow = flowOf(200, 201, 202, 203, 204, 205)
            .onEach { println("Hot: $it") }
            .shareIn(this, SharingStarted.Lazily) // Здесь превращаем Flow в SharedFlow

        launch { hotFlow.collect() }
        launch { hotFlow.collect() }

        delay(500)
        coroutineContext.cancelChildren()
    }

    /**
     * Работа с состояниями
     */
    @Test
    fun state(): Unit = runBlocking {
        val mshState = MutableStateFlow("state1")
        val shState = mshState.asStateFlow()
        val collector: FlowCollector<String> = mshState
        launch { mshState.collect { println("MUT $it") } }
        launch { shState.collect { println("IMMUT $it") } }
        launch {
            (1..20).forEach {
                delay(20)
                collector.emit("zz: $it")
            }
        }
        delay(100)
        println("FINAL STATE: ${shState.value}") // State реализует value, с помощью которого всегда можем
                                                 // получить актуальное состояние
        coroutineContext.cancelChildren()
    }
}
