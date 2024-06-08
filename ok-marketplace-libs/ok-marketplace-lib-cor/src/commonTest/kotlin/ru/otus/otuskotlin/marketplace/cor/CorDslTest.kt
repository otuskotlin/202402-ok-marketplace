package ru.otus.otuskotlin.marketplace.cor

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CorBaseTest {
    /*private suspend fun execute(dsl: ICorExecDsl<TestContext>): TestContext {
        val ctx = TestContext()
        dsl.build().exec(ctx)
        return ctx
    }

    @Test
    fun `handle should execute`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle { history += "w1; " }
            }
        }
        val ctx = execute(chain)
        assertEquals("w1; ", ctx.history)
    }

    @Test
    fun `on should check condition`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                on { status == TestContext.CorStatuses.ERROR }
                handle { history += "w1; " }
            }
            worker {
                on { status == TestContext.CorStatuses.NONE }
                handle {
                    history += "w2; "
                    status = TestContext.CorStatuses.FAILING
                }
            }
            worker {
                on { status == TestContext.CorStatuses.FAILING }
                handle { history += "w3; " }
            }
        }
        assertEquals("w2; w3; ", execute(chain).history)
    }

    @Test
    fun `except should execute when exception`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle { throw RuntimeException("some error") }
                except { history += it.message }
            }
        }
        assertEquals("some error", execute(chain).history)
    }

    @Test
    fun `should throw when exception and no except`() = runTest {
        val chain = rootChain<TestContext> {
            worker("throw") { throw RuntimeException("some error") }
        }
        assertFails {
            execute(chain)
        }
    }

    @Test
    fun `complex chain example`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                title = "Инициализация статуса"
                description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

                on { status == TestContext.CorStatuses.NONE }
                handle { status = TestContext.CorStatuses.RUNNING }
                except { status = TestContext.CorStatuses.ERROR }
            }

            chain {
                on { status == TestContext.CorStatuses.RUNNING }

                worker(
                    title = "Лямбда обработчик",
                    description = "Пример использования обработчика в виде лямбды"
                ) {
                    some += 4
                }
            }

            printResult()

        }.build()

        val ctx = TestContext()
        chain.exec(ctx)
        println("Complete: $ctx")
    }

    private fun ICorChainDsl<TestContext>.printResult() = worker(title = "Print example") {
        println("some = $some")
    }*/
}

