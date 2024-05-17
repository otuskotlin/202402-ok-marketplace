package ru.otus.otuskotlin.marketplace.biz.validation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
fun runBizTest(block: suspend () -> Unit) = runTest {
    withContext(Dispatchers.Default.limitedParallelism(1)) {
        block()
    }
}
