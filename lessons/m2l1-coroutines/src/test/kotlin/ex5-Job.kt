package ru.otus.otuskotlin.coroutines

import kotlinx.coroutines.*
import kotlin.test.Test

class Ex5Job {
    private fun CoroutineScope.createJob(name: String, start: CoroutineStart = CoroutineStart.DEFAULT) =
        launch(start = start) {
            delay(10)
            println("start $name")
            delay(1000)
            println("complete $name")
        }

    @Test
    fun join(): Unit = runBlocking {
        val job1 = createJob("1")
        job1.join()
        println("After join")
    }

    @Test
    fun manualStart(): Unit = runBlocking {
        val job1 = createJob("1", start = CoroutineStart.LAZY)
        delay(50)
        println("Manual start")
        job1.start()
    }

    @Test
    fun dontStart(): Unit = runBlocking {
        val job1 = createJob("1", start = CoroutineStart.LAZY)
        delay(50)
        job1.join()
    }

    @Test
    fun cancel(): Unit = runBlocking {
        val job1 = createJob("1")
        delay(50)
        job1.cancel()
        println("After cancel")
    }

    @Suppress("RedundantSuspendModifier")
    private suspend fun x() {
        @Suppress("BlockingMethodInNonBlockingContext")
        Thread.sleep(10)
    }

    @Test
    fun cancelTrouble(): Unit = runBlocking(Dispatchers.Default) {
        val job1 = launch {
            for (i in 1..1000) {
                x()
                //if (!isActive) break
            }
            println("Job complete")
        }
        delay(50)
        println("Before cancel")
        job1.cancel()
        println("After cancel")
    }

    @Test
    fun scope() {
        val scope = CoroutineScope(Job())
        scope.createJob("1")
        scope.createJob("2")

        Thread.sleep(500)
        scope.cancel()
    }

    @Test
    fun scopeHierarchy(): Unit = runBlocking {
        println("top $this")
        val job1 = launch {
            println("job1 block $this")

            val myJob = this.coroutineContext.job
            println("job1 myJob $myJob")

            val job2 = launch {
                println("job2 block $this")
            }
            println("job2 $job2")
        }
        println("job1 $job1")
    }
}
