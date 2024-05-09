package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.otus.otuskotlin.marketplace.common.models.*

class AdTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val title = text(SqlFields.TITLE).nullable()
    val description = text(SqlFields.DESCRIPTION).nullable()
    val owner = text(SqlFields.OWNER_ID)
    val visibility = visibilityEnumeration(SqlFields.VISIBILITY)
    val dealSide = adTypeEnumeration(SqlFields.AD_TYPE)
    val lock = text(SqlFields.LOCK)
    val productId = text(SqlFields.PRODUCT_ID).nullable()

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = MkplAd(
        id = MkplAdId(res[id].toString()),
        title = res[title] ?: "",
        description = res[description] ?: "",
        ownerId = MkplUserId(res[owner].toString()),
        visibility = res[visibility],
        adType = res[dealSide],
        lock = MkplAdLock(res[lock]),
        productId = res[productId]?.let { MkplProductId(it) } ?: MkplProductId.NONE,
    )

    fun to(it: UpdateBuilder<*>, ad: MkplAd, randomUuid: () -> String) {
        it[id] = ad.id.takeIf { it != MkplAdId.NONE }?.asString() ?: randomUuid()
        it[title] = ad.title
        it[description] = ad.description
        it[owner] = ad.ownerId.asString()
        it[visibility] = ad.visibility
        it[dealSide] = ad.adType
        it[lock] = ad.lock.takeIf { it != MkplAdLock.NONE }?.asString() ?: randomUuid()
        it[productId] = ad.productId.takeIf { it != MkplProductId.NONE }?.asString()
    }

}

