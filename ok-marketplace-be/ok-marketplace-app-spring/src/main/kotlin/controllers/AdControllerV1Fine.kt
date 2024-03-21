package ru.otus.otuskotlin.markeplace.app.spring.controllers

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.markeplace.app.spring.config.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/ad")
class AdControllerV1Fine(
    private val appSettings: MkplAppSettings
) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: AdCreateRequest): AdCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun  read(@RequestBody request: AdReadRequest): AdReadResponse =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  update(@RequestBody request: AdUpdateRequest): AdUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun  delete(@RequestBody request: AdDeleteRequest): AdDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun  search(@RequestBody request: AdSearchRequest): AdSearchResponse =
        process(appSettings, request = request, this::class, "search")

    @PostMapping("offers")
    suspend fun  offers(@RequestBody request: AdOffersRequest): AdOffersResponse =
        process(appSettings, request = request, this::class, "offers")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: MkplAppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request)
            },
            { toTransportAd() as R },
            clazz,
            logId,
        )
    }
}
