package ru.otus.otuskotlin.flows

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import java.time.Instant
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.test.Test
import kotlin.test.assertEquals

class FlowOperatorsTest {

    /**
     * Простейшая цепочка flow
     */
    @Test
    fun simple(): Unit = runBlocking {
        flowOf(1, 2, 3, 4) // билдер
            .onEach { println(it) } // операции ...
            .map { it + 1 }
            .filter { it % 2 == 0 }
            .collect { println("Result number $it") } // терминальный оператор
    }

    /**
     * Хелпер-функция для печати текущего потока
     */
    private fun <T> Flow<T>.printThreadName(msg: String) =
        this.onEach { println("Msg = $msg, thread name = ${Thread.currentThread().name}") }

    /**
     * Демонстрация переключения корутин-контекста во flow
     */
    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    @Test
    fun coroutineContextChange(): Unit = runBlocking {
        // Просто создали диспетчера и безопасно положили его в apiDispatcher
        newSingleThreadContext("Api-Thread").use { apiDispatcher ->
            // еще один...
            newSingleThreadContext("Db-Thread").use { dbDispatcher ->

                // Контекст переключается в ОБРАТНОМ ПОРЯДКЕ, т.е. СНИЗУ ВВЕРХ
                flowOf(10, 20, 30) // apiDispatcher
                    .filter { it % 2 == 0 } // apiDispatcher
                    .map {
                        delay(2000)
                        it
                    } // apiDispatcher
                    .printThreadName("api call") // apiDispatcher
                    .flowOn(apiDispatcher) // Переключаем контекст выполнения на apiDispatcher
                    .map { it + 1 } // dbDispatcher
                    .printThreadName("db call") // dbDispatcher
                    .flowOn(dbDispatcher) // Переключаем контекст выполнения на dbDispatcher
                    .printThreadName("last operation") // Default
                    .onEach { println("On each $it") } // Default
                    .collect() // Запустится в контексте по умолчанию, т.е. в Dispatchers.Default
            }
        }
    }

    /**
     * Демонстрация тригеров onStart, onCompletion, catch, onEach
     */
    @Test
    fun startersStopers(): Unit = runBlocking {
        flow {
            while (true) {
                emit(1)
                delay(1000)
                emit(2)
                delay(1000)
                emit(3)
                delay(1000)
                throw RuntimeException("Custom error!")
            }
        }
            .onStart { println("On start") } // Запустится один раз только вначале
            .catch { println("Catch: ${it.message}") } // Запустится только при генерации исключения
            .onEach { println("On each: $it") } // Генерируется для каждого сообщения
            .onCompletion { println(" On completion") } // Запустится один раз только вконце
            .collect { }
    }


    /**
     * Демонстрация буферизации.
     * Посмотрите как меняется порядок следования сообщений при применении буфера.
     * Буфер можно выставить в 0, либо поставить любое положительное значение.
     * Попробуйте поменять тип буфера и посмотрите как изменится поведение. Лучше менять при размере буфера 3.
     * Имейте в виду, что инициализация генерации и обработки элемента в цепочке всегда происходит в терминальном
     * операторе.
     */
    @Test
    fun buffering(): Unit = runBlocking {
        val timeInit = System.currentTimeMillis()
        var sleepIndex = 1 // Счетчик инкрементится в терминальном операторе после большой задержки
        flowOf(1, 2, 3, 4, 5)
            .onEach { println("Send to flow: $it ${System.currentTimeMillis() - timeInit}ms") }
            .buffer(3, BufferOverflow.DROP_LATEST) // Здесь включаем буфер размером 3 элемента
//            .buffer(3, BufferOverflow.DROP_OLDEST) // Попробуйте разные варианты типов и размеров буферов
//            .buffer(3, BufferOverflow.SUSPEND)
            .onEach { println("Processing : $it ${System.currentTimeMillis() - timeInit}ms") }
            .collect {
                println("Sleep(${sleepIndex++}) got $it / ${System.currentTimeMillis() - timeInit}ms")
                delay(2_000)
            }
    }

    /**
     * Демонстрация реализации кастомного оператора для цепочки.
     */
    @Test
    fun customOperator(): Unit = runBlocking {
        fun <T> Flow<T>.zipWithNext(): Flow<Pair<T, T>> = flow {
            var prev: T? = null
            collect { el ->
                prev?.also { pr -> emit(pr to el) } // Здесь корректная проверка на NULL при использовании var
                prev = el
            }
        }

        flowOf(1, 2, 3, 4)
            .zipWithNext()
            .collect { println(it) }
    }

    /**
     * Терминальный оператор toList.
     * Попробуйте другие: collect, toSet, first, single (потребуется изменить билдер)
     */
    @Test
    fun toListTermination(): Unit = runBlocking {
        val list = flow {
            emit(1)
            delay(100)
            emit(2)
            delay(100)
        }
            .onEach { println("$it") }
            .toList()

        println("List: $list")
    }

    /**
     * Работа с бесконечными билдерами flow
     */
    @Test
    fun infiniteBuilder(): Unit = runBlocking {
        val list = flow {
            var index = 0
            // здесь бесконечный цикл, не переполнения не будет из-за take
            while (true) {
                emit(index++)
                delay(100)
            }
        }
            .onEach { println("$it") }
            .take(10) // Попробуйте поменять аргумент и понаблюдайте за размером результирующего списка
            .toList()

        println("List: $list")
    }

    /**
     * Преобразование событий колбэка во flow.
     * Здесь используются Kotlin Channels.
     * В общем случае каналы использовать не рекомендуется
     */
    @Test
    fun callbackFlowTest(): Unit = runBlocking {
        val fl = callbackFlow {
            val timer = Timer()
            timer.scheduleAtFixedRate(6L, 1000L) {
                val time = Instant.ofEpochMilli(this.scheduledExecutionTime())
                this@callbackFlow.trySendBlocking("text $time")
            }
            awaitClose { timer.cancel() }
        }
        fl.take(3).collect {
            println("RES: $it")
        }
        assertEquals(3, fl.take(3).count())
    }

}


