package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide

fun Table.adTypeEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.AD_TYPE_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.AD_TYPE_DEMAND -> MkplDealSide.DEMAND
            SqlFields.AD_TYPE_SUPPLY -> MkplDealSide.SUPPLY
            else -> MkplDealSide.NONE
        }
    },
    toDb = { value ->
        when (value) {
            MkplDealSide.DEMAND -> PgAdTypeDemand
            MkplDealSide.SUPPLY -> PgAdTypeSupply
            MkplDealSide.NONE -> throw Exception("Wrong value of Ad Type. NONE is unsupported")
        }
    }
)

sealed class PgAdTypeValue(enVal: String): PGobject() {
    init {
        type = SqlFields.AD_TYPE_TYPE
        value = enVal
    }
}

object PgAdTypeDemand: PgAdTypeValue(SqlFields.AD_TYPE_DEMAND)
object PgAdTypeSupply: PgAdTypeValue(SqlFields.AD_TYPE_SUPPLY)
