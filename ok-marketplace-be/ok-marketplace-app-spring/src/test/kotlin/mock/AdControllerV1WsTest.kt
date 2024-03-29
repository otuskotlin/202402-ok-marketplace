package ru.otus.otuskotlin.markeplace.app.spring.mock

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import ru.otus.otuskotlin.markeplace.app.spring.mock.AdControllerBaseWsTest
import ru.otus.otuskotlin.marketplace.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.marketplace.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("unused")
class AdControllerV1WsTest : AdControllerBaseWsTest<IRequest, IResponse>("v1") {

    @LocalServerPort
    override var port: Int = 0

    override fun deserializeRs(response: String): IResponse = apiV1ResponseDeserialize(response)
    override fun serializeRq(request: IRequest): String = apiV1RequestSerialize(request)

    @Test
    fun wsCreate(): Unit = testWsApp(
        AdCreateRequest(
            debug = AdDebug(AdRequestDebugMode.STUB, AdRequestDebugStubs.SUCCESS),
            ad = AdCreateObject(
                title = "test1",
                description = "desc",
                adType = DealSide.DEMAND,
                visibility = AdVisibility.PUBLIC,
            )
        )
    ) { pl ->
        val mesInit = pl[0]
        val mesCreate = pl[1]
        assert(mesInit is AdInitResponse)
        assert(mesInit.result == ResponseResult.SUCCESS)
        assert(mesCreate is AdCreateResponse)
        assert(mesCreate.result == ResponseResult.SUCCESS)
    }
}
