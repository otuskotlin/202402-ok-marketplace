package ru.otus.otuskotlin.coroutines

import org.junit.Test
import java.util.concurrent.CompletableFuture

class Ex2Future {
    private fun someMethod(): CompletableFuture<String> = CompletableFuture.supplyAsync {
        println("Some method")
        Thread.sleep(1000)
        //throw RuntimeException("Error")
        "Some data"
    }

    private fun otherMethod(a: Int, throwPlace: Int = 0): CompletableFuture<Int> {
        if (throwPlace == 1) throw RuntimeException("1")
        return CompletableFuture.supplyAsync {
            println("Other method")
            if (throwPlace == 2) throw RuntimeException("2")
            Thread.sleep(1000)
            a * 2
        }
    }

    @Test
    fun future() {
        someMethod()
            .thenApply {
                println("Apply")
                it.length
            }
            .thenCompose {
                otherMethod(it)
            }
            .handle { num, ex ->
                if (ex != null) {
                    println("Exception $ex")
                } else {
                    println("Complete $num")
                }
            }
            .get()

        println("Complete")

        //Thread.sleep(3000)
    }

    @Test
    fun exception() {
        CompletableFuture.completedFuture(42)
            .thenCompose {
                try {
                    otherMethod(it, 1)
                } catch (e: Exception) {
                    println("ThenCompose $e")
                    CompletableFuture.completedFuture(42)
                }
            }
            .get()

        println("Complete")
    }
}
