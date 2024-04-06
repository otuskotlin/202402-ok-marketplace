package ru.otus.otuskotlin.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test

class Ex6Scope {
    @Test
    fun create1() {
        val scope = CoroutineScope(Dispatchers.Main)

        println("scope: $scope")
    }

    @Test
    fun create2() {
        val scope = CoroutineScope(Dispatchers.Main + Job() + CoroutineName("create2"))

        println("scope: $scope")
    }

    @Test
    fun create3() {
        val scope = CoroutineScope(SomeData(10, 20))
        val data = scope.coroutineContext[SomeData]

        println("scope: $scope, $data")
    }

    private fun CoroutineScope.scopeToString(): String =
        "Job = ${coroutineContext[Job]}, Dispatcher = ${coroutineContext[ContinuationInterceptor]}, Data = ${coroutineContext[SomeData]}"

    @Test
    fun defaults() {
        val scope = CoroutineScope(SomeData(10, 20))
        scope.launch {
            println("Child1: ${scopeToString()}")
        }
        scope.launch(SomeData(1, 2)) {
            println("Child2: ${scopeToString()}")
        }

        println("This: ${scope.scopeToString()}")
    }

    data class SomeData(val x: Int, val y: Int) : AbstractCoroutineContextElement(SomeData) {
        companion object : CoroutineContext.Key<SomeData>
    }
}
