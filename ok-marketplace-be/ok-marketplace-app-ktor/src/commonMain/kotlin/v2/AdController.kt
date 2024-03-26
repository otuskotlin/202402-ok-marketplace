package ru.otus.otuskotlin.marketplace.app.ktor.v2

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createAd::class
suspend fun ApplicationCall.createAd(appSettings: MkplAppSettings) =
    processV2<AdCreateRequest, AdCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::createAd::class
suspend fun ApplicationCall.readAd(appSettings: MkplAppSettings) =
    processV2<AdReadRequest, AdReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::createAd::class
suspend fun ApplicationCall.updateAd(appSettings: MkplAppSettings) =
    processV2<AdUpdateRequest, AdUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::createAd::class
suspend fun ApplicationCall.deleteAd(appSettings: MkplAppSettings) =
    processV2<AdDeleteRequest, AdDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::createAd::class
suspend fun ApplicationCall.searchAd(appSettings: MkplAppSettings) =
    processV2<AdSearchRequest, AdSearchResponse>(appSettings, clSearch, "search")

val clOffers: KClass<*> = ApplicationCall::createAd::class
suspend fun ApplicationCall.offersAd(appSettings: MkplAppSettings) =
    processV2<AdOffersRequest, AdOffersResponse>(appSettings, clOffers, "offers")
