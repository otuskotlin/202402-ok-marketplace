package ru.otus.otuskotlin.marketplace.app.ktor.auth

import io.ktor.client.request.*
import ru.otus.otuskotlin.marketplace.app.common.AUTH_HEADER
import ru.otus.otuskotlin.marketplace.app.common.createJwtTestHeader
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserGroups
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStubBolts.AD_DEMAND_BOLT1

fun HttpRequestBuilder.addAuth(principal: MkplPrincipalModel) {
    header(AUTH_HEADER, principal.createJwtTestHeader())
}

fun HttpRequestBuilder.addAuth(
    id: MkplUserId = AD_DEMAND_BOLT1.ownerId,
    groups: Collection<MkplUserGroups> = listOf(MkplUserGroups.TEST, MkplUserGroups.USER),
) {
    addAuth(MkplPrincipalModel(id, groups = groups.toSet()))
}
