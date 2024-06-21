package ru.otus.otuskotlin.markeplace.app.spring.repo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import ru.otus.otuskotlin.markeplace.app.spring.config.AdConfig
import ru.otus.otuskotlin.markeplace.app.spring.controllers.AdControllerV1Fine
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.common.repo.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdRequest
import ru.otus.otuskotlin.marketplace.common.repo.IRepoAd
import ru.otus.otuskotlin.marketplace.repo.common.AdRepoInitialized
import ru.otus.otuskotlin.marketplace.repo.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(AdControllerV1Fine::class, AdConfig::class)
internal class AdRepoInMemoryV1Test : AdRepoBaseV1Test() {
    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepoAd

    @BeforeEach
    fun tearUp() {
        val slotAd = slot<DbAdRequest>()
        val slotId = slot<DbAdIdRequest>()
        val slotFl = slot<DbAdFilterRequest>()
        val repo = AdRepoInitialized(
            repo = AdRepoInMemory(randomUuid = { uuidNew }),
            initObjects = MkplAdStub.prepareSearchList("xx", MkplDealSide.SUPPLY) + MkplAdStub.get()
        )
        coEvery { testTestRepo.createAd(capture(slotAd)) } coAnswers { repo.createAd(slotAd.captured) }
        coEvery { testTestRepo.readAd(capture(slotId)) } coAnswers { repo.readAd(slotId.captured) }
        coEvery { testTestRepo.updateAd(capture(slotAd)) } coAnswers { repo.updateAd(slotAd.captured) }
        coEvery { testTestRepo.deleteAd(capture(slotId)) } coAnswers { repo.deleteAd(slotId.captured) }
        coEvery { testTestRepo.searchAd(capture(slotFl)) } coAnswers { repo.searchAd(slotFl.captured) }
    }

    @Test
    override fun createAd() = super.createAd()

    @Test
    override fun readAd() = super.readAd()

    @Test
    override fun updateAd() = super.updateAd()

    @Test
    override fun deleteAd() = super.deleteAd()

    @Test
    override fun searchAd() = super.searchAd()

    @Test
    override fun offersAd() = super.offersAd()
}
