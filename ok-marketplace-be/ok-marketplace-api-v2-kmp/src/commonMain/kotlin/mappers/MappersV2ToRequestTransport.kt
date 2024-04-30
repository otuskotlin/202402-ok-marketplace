package ru.otus.otuskotlin.marketplace.api.v2.mappers

import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.common.models.*

fun MkplAd.toTransportCreate() = AdCreateObject(
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    adType = adType.toTransportAd(),
    visibility = visibility.toTransportAd(),
)

fun MkplAd.toTransportRead() = AdReadObject(
    id = id.takeIf { it != MkplAdId.NONE }?.asString(),
)

fun MkplAd.toTransportUpdate() = AdUpdateObject(
    id = id.takeIf { it != MkplAdId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    adType = adType.toTransportAd(),
    visibility = visibility.toTransportAd(),
    lock = lock.takeIf { it != MkplAdLock.NONE }?.asString(),
)

fun MkplAd.toTransportDelete() = AdDeleteObject(
    id = id.takeIf { it != MkplAdId.NONE }?.asString(),
    lock = lock.takeIf { it != MkplAdLock.NONE }?.asString(),
)
