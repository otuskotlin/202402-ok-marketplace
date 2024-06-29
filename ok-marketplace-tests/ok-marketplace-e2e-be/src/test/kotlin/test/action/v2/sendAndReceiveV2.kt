package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v2

import co.touchlab.kermit.Logger
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestSimpleSerialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSimpleDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.client.Client


suspend inline fun <reified Rq: IRequest, reified Rs: IResponse> Client.sendAndReceive(path: String, request: Rq): Rs {
    val log = Logger
    val requestBody = apiV2RequestSimpleSerialize(request)
//    val requestBody = apiV2RequestSerialize(request)
    log.w { "Send to v2/$path\n$requestBody" }

    val responseBody = sendAndReceive("v2", path, requestBody)
    log.w { "Received\n$responseBody" }

//    return apiV2ResponseDeserialize(responseBody)
    return apiV2ResponseSimpleDeserialize(responseBody)
}
