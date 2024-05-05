package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import io.github.moreirasantos.pgkn.resultset.ResultSet
import ru.otus.otuskotlin.marketplace.common.models.*

internal fun ResultSet.fromDb(cols: List<String>): MkplAd {
    val fieldsMap = cols.mapIndexed { i: Int, field: String -> field to i }.toMap()
    fun col(field: String): String? = fieldsMap[field]?.let { getString(it) }
    return MkplAd(
        id = col(SqlFields.ID)?.let { MkplAdId(it) } ?: MkplAdId.NONE,
        title = col(SqlFields.TITLE) ?: "",
        description = col(SqlFields.DESCRIPTION) ?: "",
        ownerId = col(SqlFields.OWNER_ID)?.let { MkplUserId(it) } ?: MkplUserId.NONE,
        adType = col(SqlFields.AD_TYPE).asAdType(),
        visibility = col(SqlFields.VISIBILITY).asVisibility(),
        productId = col(SqlFields.PRODUCT_ID)?.let { MkplProductId(it) } ?: MkplProductId.NONE,
        lock = col(SqlFields.LOCK)?.let { MkplAdLock(it) } ?: MkplAdLock.NONE,
    )
}

private fun String?.asAdType(): MkplDealSide = when (this) {
    SqlFields.AD_TYPE_DEMAND -> MkplDealSide.DEMAND
    SqlFields.AD_TYPE_SUPPLY -> MkplDealSide.SUPPLY
    else -> MkplDealSide.NONE
}

private fun String?.asVisibility(): MkplVisibility = when (this) {
    SqlFields.VISIBILITY_OWNER -> MkplVisibility.VISIBLE_TO_OWNER
    SqlFields.VISIBILITY_GROUP -> MkplVisibility.VISIBLE_TO_GROUP
    SqlFields.VISIBILITY_PUBLIC -> MkplVisibility.VISIBLE_PUBLIC
    else -> MkplVisibility.NONE
}
