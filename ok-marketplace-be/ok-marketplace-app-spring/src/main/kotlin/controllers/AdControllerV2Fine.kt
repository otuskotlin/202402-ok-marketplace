package ru.otus.otuskotlin.markeplace.app.spring.controllers

import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.markeplace.app.spring.base.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v2.mappers.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.common.AUTH_HEADER
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.app.common.jwt2principal
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v2/ad")
class AdControllerV2Fine(private val appSettings: MkplAppSettings) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: AdCreateRequest, @RequestHeader headers: HttpHeaders): AdCreateResponse =
        process(appSettings, request = request, headers = headers, this::class, "create")

    @PostMapping("read")
    suspend fun  read(@RequestBody request: AdReadRequest, @RequestHeader headers: HttpHeaders): AdReadResponse =
        process(appSettings, request = request, headers = headers, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  update(@RequestBody request: AdUpdateRequest, @RequestHeader headers: HttpHeaders): AdUpdateResponse =
        process(appSettings, request = request, headers = headers, this::class, "update")

    @PostMapping("delete")
    suspend fun  delete(@RequestBody request: AdDeleteRequest, @RequestHeader headers: HttpHeaders): AdDeleteResponse =
        process(appSettings, request = request, headers = headers, this::class, "delete")

    @PostMapping("search")
    suspend fun  search(@RequestBody request: AdSearchRequest, @RequestHeader headers: HttpHeaders): AdSearchResponse =
        process(appSettings, request = request, headers = headers, this::class, "search")

    @PostMapping("offers")
    suspend fun  offers(@RequestBody request: AdOffersRequest, @RequestHeader headers: HttpHeaders): AdOffersResponse =
        process(appSettings, request = request, headers = headers, this::class, "offers")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: MkplAppSettings,
            request: Q,
            headers: HttpHeaders,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                principal = headers[AUTH_HEADER]?.first().jwt2principal()
                fromTransport(request)
            },
            { toTransportAd() as R },
            clazz,
            logId,
        )
    }
}
