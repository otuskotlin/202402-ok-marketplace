package ru.otus.otuskotlin.coroutines

import kotlinx.coroutines.*
import kotlin.test.Test

class Ex8Exceptions {
    @Test
    fun invalid() {
        try {
            val scope = CoroutineScope(Dispatchers.Default)
            scope.launch {
                Integer.parseInt("a")
            }
        } catch (e: Exception) {
            println("CAUGHT!")
        }

        Thread.sleep(2000)
        println("COMPLETED!")
    }

    @Test
    fun invalid2() {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            try {
                launch {
                    Integer.parseInt("a")
                }
            } catch (e: Exception) {
                println("CAUGHT!")
            }
        }

        Thread.sleep(2000)
        println("COMPLETED!")
    }

    private fun handler(where: String) = CoroutineExceptionHandler { context, exception ->
        println("CAUGHT at $where ${context[CoroutineName]}: $exception")
    }

    @Test
    fun handler() {
        val scope = CoroutineScope(Dispatchers.Default + handler("top"))
        scope.launch(handler("launch")) {
            Integer.parseInt("a")
        }

        Thread.sleep(2000)
        println("COMPLETED!")
    }

    @Test
    fun handler2() {
        val scope = CoroutineScope(Dispatchers.Default + handler("top"))
        scope.launch(CoroutineName("1")) {
            launch(handler("child") + CoroutineName("1.1")) {
                Integer.parseInt("a")
            }
        }

        Thread.sleep(2000)
        println("COMPLETED!")
    }

    @Test
    fun cancel() {
        val scope = CoroutineScope(Dispatchers.Default + handler("top"))
        scope.launch {

            launch { // 3 если сюда добавить handler(), то ничего не изменится
                launch {
                    delay(100) // 1 дочерние отменены
                    println("cor1")
                }
                launch {
                    delay(100)
                    println("cor2")
                }

                Integer.parseInt("a")
            }
            launch { // 2 сиблинг тоже отменен
                delay(100)
                println("cor3")
            }
        }

        Thread.sleep(2000)

        scope.launch {
            // 4 scope отменен, в нем больше ничего не запустить
            println("No chancel")
        }

        val scope2 = CoroutineScope(Dispatchers.Default)
        scope2.launch { // другой скоуп никак не затронут
            println("I am alive")
        }

        Thread.sleep(500)

        println("COMPLETED!")
    }

    @Test
    fun supervisorJob() {
        val scope = CoroutineScope(Dispatchers.Default + handler("top") + SupervisorJob())
        scope.launch {
            launch {
                delay(100) // 1 сиблинги отменены
                println("cor1")
            }
            launch {
                delay(100)
                println("cor2")
            }
            launch {
                delay(50)
                Integer.parseInt("a")
            }

            delay(100)
            // 1 сама джоба отменена
            println("super")
        }

        Thread.sleep(2000)

        scope.launch {
            // 3 жив
            println("I am alive")
        }


        Thread.sleep(500)

        println("COMPLETED!")
    }

    @Test
    fun supervisorJob2() {
        val scope = CoroutineScope(Dispatchers.Default + handler("top") + SupervisorJob())
        scope.launch {// ***
            launch {
                delay(100)
                println("cor1")
            }
            launch(SupervisorJob()) {
                delay(50)
                Integer.parseInt("a") // 1 - комментируем
                // 1 - ops
                println("cor2")
            }

            delay(100)
            println("super")
        }

        // scope.cancel() // 1 - раскомментируем

        Thread.sleep(2000)

        println("COMPLETED!")
    }

    @Test
    fun supervisorJob3() {
        val scope = CoroutineScope(Dispatchers.Default + handler("top"))
        scope.launch {// ***
            launch {
                delay(100)
                println("cor1")
            }
            launch(SupervisorJob(coroutineContext.job)) {
                launch {
                    delay(10)
                    Integer.parseInt("a") // 1 - комментируем
                }
                launch {
                    delay(50)
                    // отменится
                    println("cor2")
                }
                delay(50)
                // отменится
                println("cor3")
            }

            delay(100)
            println("super")
        }

        // scope.cancel() // 1 - раскомментируем

        Thread.sleep(2000)

        println("COMPLETED!")
    }

    @Test
    fun handler3() {
        val scope = CoroutineScope(Dispatchers.Default + handler("top"))
        scope.launch(CoroutineName("1")) {
            launch(handler("child") + CoroutineName("1.1") + SupervisorJob(coroutineContext.job)) {
                Integer.parseInt("a")
            }
        }

        Thread.sleep(2000)
        println("COMPLETED!")
    }

    @Test
    fun async1() {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            launch {
                delay(100)
                println("cor1")
            }
            val x = async {
                Integer.parseInt("a")
            }

            delay(100)

            println("1")
            try {
                x.await()
            } catch (e: Exception) {
                println("CAUGHT!")
            }
        }

        Thread.sleep(2000)

        println("COMPLETED!")
    }

    @Test
    fun async2() {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            launch {
                delay(100)
                println("cor1")
            }
            val x = async(SupervisorJob(coroutineContext.job) + handler("async")) {
                Integer.parseInt("a")
            }

            delay(100)

            println("1")
            try {
                x.await()
            } catch (e: Exception) {
                println("CAUGHT!")
            }
        }

        Thread.sleep(2000)

        println("COMPLETED!")
    }

}
