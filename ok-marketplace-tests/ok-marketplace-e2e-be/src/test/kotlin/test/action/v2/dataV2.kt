package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.e2e.be.test.TestDebug

val debugStubV2: AdDebug = AdDebug(mode = AdRequestDebugMode.STUB, stub = AdRequestDebugStubs.SUCCESS)

val someCreateAd = AdCreateObject(
    title = "Требуется болт",
    description = "Требуется болт 100x5 с шестигранной шляпкой",
    adType = DealSide.DEMAND,
    visibility = AdVisibility.PUBLIC
)

fun TestDebug.toV2() = when(this) {
    TestDebug.STUB -> debugStubV2
    TestDebug.PROD -> AdDebug(mode = AdRequestDebugMode.PROD)
    TestDebug.TEST -> AdDebug(mode = AdRequestDebugMode.TEST)
}
