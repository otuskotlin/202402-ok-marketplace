package ru.otus.otuskotlin.marketplace.e2e.be.test

import io.kotest.assertions.asClue
import io.kotest.assertions.fail
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldExistInOrder
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.marketplace.api.v1.models.AdDebug
import ru.otus.otuskotlin.marketplace.api.v1.models.AdSearchFilter
import ru.otus.otuskotlin.marketplace.api.v1.models.AdUpdateObject
import ru.otus.otuskotlin.marketplace.api.v1.models.DealSide
import ru.otus.otuskotlin.marketplace.e2e.be.fixture.client.Client
import ru.otus.otuskotlin.marketplace.e2e.be.test.action.v1.*

fun FunSpec.testApiV1(client: Client, prefix: String = "", debug: AdDebug = debugStubV1) {
    context("${prefix}v1") {
        test("Create Ad ok") {
            client.createAd(debug = debug)
        }

        test("Read Ad ok") {
            val created = client.createAd(debug = debug)
            client.readAd(created.id, debug = debug).asClue {
                it shouldBe created
            }
        }

        test("Update Ad ok") {
            val created = client.createAd(debug = debug)
            val updateAd = AdUpdateObject(
                id = created.id,
                lock = created.lock,
                title = "Selling Nut",
                description = created.description,
                adType = created.adType,
                visibility = created.visibility,
            )
            client.updateAd(updateAd, debug = debug)
        }

        test("Delete Ad ok") {
            val created = client.createAd(debug = debug)
            client.deleteAd(created, debug = debug)
//            client.readAd(created.id) {
//                 it should haveError("not-found")
//            }
        }

        test("Search Ad ok") {
            val created1 = client.createAd(someCreateAd.copy(title = "Selling Bolt"), debug = debug)
            val created2 = client.createAd(someCreateAd.copy(title = "Selling Nut"), debug = debug)

            withClue("Search Selling") {
                val results = client.searchAd(search = AdSearchFilter(searchString = "Selling"), debug = debug)
                results shouldExist { it.title == created1.title }
                results shouldExist { it.title == created2.title }
            }

            withClue("Search Bolt") {
                client.searchAd(search = AdSearchFilter(searchString = "Bolt"), debug = debug)
                    .shouldExistInOrder({ it.title == created1.title })
            }
        }

        test("Offer Ad ok") {
            val supply = client.createAd(someCreateAd.copy(title = "Some Bolt", adType = DealSide.SUPPLY), debug = debug)
            val demand = client.createAd(someCreateAd.copy(title = "Some Bolt", adType = DealSide.DEMAND), debug = debug)

            withClue("Find offer for supply") {
                val res1 = client.offersAd(supply.id, debug = debug)
                res1.ad?.adType shouldBe supply.adType
                res1.ads?.shouldExistInOrder({ it.adType == demand.adType }) ?: fail("Empty ads")
            }
        }
    }

}
