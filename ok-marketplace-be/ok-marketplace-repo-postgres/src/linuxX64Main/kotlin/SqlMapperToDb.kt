package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import ru.otus.otuskotlin.marketplace.common.models.*

private fun String.toDb() = this.takeIf { it.isNotBlank() }

internal fun MkplAd.toDb() = mapOf(
    SqlFields.ID to id.asString().toDb(),
    SqlFields.TITLE to title.toDb(),
    SqlFields.DESCRIPTION to description.toDb(),
    SqlFields.OWNER_ID to ownerId.asString().toDb(),
    SqlFields.AD_TYPE to adType.toDb(),
    SqlFields.VISIBILITY to visibility.toDb(),
    SqlFields.PRODUCT_ID to productId.asString().toDb(),
    SqlFields.LOCK to lock.asString().toDb(),
)

private fun MkplDealSide.toDb() = when (this) {
    MkplDealSide.DEMAND -> SqlFields.AD_TYPE_DEMAND
    MkplDealSide.SUPPLY -> SqlFields.AD_TYPE_SUPPLY
    MkplDealSide.NONE -> null
}

private fun MkplVisibility.toDb() = when (this) {
    MkplVisibility.VISIBLE_TO_OWNER -> SqlFields.VISIBILITY_OWNER
    MkplVisibility.VISIBLE_TO_GROUP -> SqlFields.VISIBILITY_GROUP
    MkplVisibility.VISIBLE_PUBLIC -> SqlFields.VISIBILITY_PUBLIC
    MkplVisibility.NONE -> null
}
