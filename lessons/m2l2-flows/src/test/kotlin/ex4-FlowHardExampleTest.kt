package ru.otus.otuskotlin.flows

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.otus.otuskotlin.flows.homework.*
import kotlin.test.Test
import java.time.Instant
import kotlin.random.Random

/**
 * Повышенная сложность.
 * Поизучайте различные виды реализации получения данных: flow, блокирующий (flow + Thread.sleep), callback.
 * Посмотрите как отражается на производительности реализация.
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class FlowTest {
    private fun detectors() : List<Detector> {
        val random = Random.Default
        val seq = sequence {
            while (true) {
                yield(random.nextDouble())
            }
        }

        return listOf(
            CoroutineDetector("coroutine", seq, 500L),
            BlockingDetector("blocking", seq, 800L),
            CallbackDetector("callback", seq, 2_000L)
        )
    }

    @Test
    fun rawDetectorsData(): Unit = runBlocking {
        // сырые данные от датчиков
        detectors()
            .map { it.samples() }
            .merge()
            .onEach { println(it) }
            .launchIn(this)

        delay(2000)
        coroutineContext.cancelChildren()
    }

    @Test
    fun oncePerSecondOrLast(): Unit = runBlocking {
        // данные от датчиков раз в секунду от каждого (если нового нет, то последнее)
        val desiredPeriod = 1000L
        detectors()
            .map {
                it.samples()
                    .transformLatest { sample ->
                        //println("Start transformLatest for ${sample.serialNumber}")
                        emit(sample)
                        while (true) {
                            delay(desiredPeriod)
                            //println("Add old value to flow in transformLatest for = ${sample.serialNumber}")
                            emit(sample.copy(timestamp = Instant.now()))
                        }
                    }
                    .sample(desiredPeriod)
            }
            .merge()
            .onEach { println(it) }
            .launchIn(this)

        delay(5_000)
        coroutineContext.cancelChildren()
    }

    @Test
    fun launchIn(): Unit = runBlocking {
        val desiredPeriod = 1000L
        val samples = detectors()
            .map {
                it.samples()
                    .transformLatest { sample ->
//                    println("Start transformLatest for ${sample.serialNumber}")
                        emit(sample)
                        while (true) {
                            delay(desiredPeriod)
//                        println("Add old value to flow in transformLatest for = ${sample.serialNumber}")
                            emit(sample.copy(timestamp = Instant.now()))
                        }
                    }
                    .sample(desiredPeriod)
            }
            .merge()
            .shareIn(this, SharingStarted.Lazily)

        samples
            .rollingMax(compareBy { it.value })
            .sample(desiredPeriod)
            .onEach { println(it) }
            .launchIn(this)

        delay(5_000)
        coroutineContext.cancelChildren()
    }

    @Test
    fun shareOncePerSecondData(): Unit = runBlocking {
        val desiredPeriod = 1000L
        val flows = detectors()
            .map {
                it.samples()
                    .transformLatest { sample ->
                        emit(sample)
                        while (true) {
                            delay(desiredPeriod)
                            emit(sample.copy(timestamp = Instant.now()))
                        }
                    }
                    .sample(desiredPeriod)
            }

        var index = 0
        val samples = combineTransform(flows) {
            it.forEach { s -> println("$index: value = $s") }
            index++

            emit(it.maxBy { s -> s.value })
        }
            .shareIn(this, SharingStarted.Lazily)

        samples
            .onEach { println(it) }
            .launchIn(this)

        delay(5_000)
        coroutineContext.cancelChildren()
    }
}
