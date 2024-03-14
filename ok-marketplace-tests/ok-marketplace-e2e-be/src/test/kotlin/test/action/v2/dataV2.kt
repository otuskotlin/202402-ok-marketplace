package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.*


val debug = AdDebug(mode = AdRequestDebugMode.STUB, stub = AdRequestDebugStubs.SUCCESS)

val someCreateAd = AdCreateObject(
    title = "Требуется болт",
    description = "Требуется болт 100x5 с шестигранной шляпкой",
    adType = DealSide.DEMAND,
    visibility = AdVisibility.PUBLIC
)
