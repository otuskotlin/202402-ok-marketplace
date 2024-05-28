package ru.otus.otuskotlin.marketplace.biz

import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserGroups
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStubBolts

fun MkplContext.addTestPrincipal(userId: MkplUserId = MkplAdStubBolts.AD_DEMAND_BOLT1.ownerId) {
    principal = MkplPrincipalModel(
        id = userId,
        groups = setOf(
            MkplUserGroups.USER,
            MkplUserGroups.TEST,
        )
    )
}
