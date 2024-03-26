package ru.otus.otuskotlin.marketplace.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings

fun Route.v1Ad(appSettings: MkplAppSettings) {
    route("ad") {
        post("create") {
            call.createAd(appSettings)
        }
        post("read") {
            call.readAd(appSettings)
        }
        post("update") {
            call.updateAd(appSettings)
        }
        post("delete") {
            call.deleteAd(appSettings)
        }
        post("search") {
            call.searchAd(appSettings)
        }
        post("offers") {
            call.offersAd(appSettings)
        }
    }
}
