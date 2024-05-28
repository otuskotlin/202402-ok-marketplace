package ru.otus.otuskotlin.marketplace.app.ktor.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.marketplace.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.marketplace.api.v2.mappers.toTransportAd
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.app.common.AUTH_HEADER
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.app.common.jwt2principal
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV2(
    appSettings: MkplAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        principal = this@processV2.request.header(AUTH_HEADER).jwt2principal()
        fromTransport(this@processV2.receive<Q>())
    },
    { this@processV2.respond(toTransportAd() as R) },
    clazz,
    logId,
)
