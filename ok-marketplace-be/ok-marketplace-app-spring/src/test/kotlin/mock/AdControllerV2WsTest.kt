package ru.otus.otuskotlin.markeplace.app.spring.mock

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import ru.otus.otuskotlin.markeplace.app.spring.mock.AdControllerBaseWsTest
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestSerialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseDeserialize
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("unused")
class AdControllerV2WsTest: AdControllerBaseWsTest<IRequest, IResponse>("v2") {

    @LocalServerPort
    override var port: Int = 0

    override fun deserializeRs(response: String): IResponse = apiV2ResponseDeserialize(response)
    override fun serializeRq(request: IRequest): String = apiV2RequestSerialize(request)

    @Test
    fun wsCreate(): Unit = testWsApp(AdCreateRequest(
        debug = AdDebug(AdRequestDebugMode.STUB, AdRequestDebugStubs.SUCCESS),
        ad = AdCreateObject(
            title = "test1",
            description = "desc",
            adType = DealSide.DEMAND,
            visibility = AdVisibility.PUBLIC,
        )
    )) { pl ->
        val mesInit = pl[0]
        val mesCreate = pl[1]
        assert(mesInit is AdInitResponse)
        assert(mesInit.result == ResponseResult.SUCCESS)
        assert(mesCreate is AdCreateResponse)
        assert(mesCreate.result == ResponseResult.SUCCESS)
    }
}
