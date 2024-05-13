package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility

fun Table.visibilityEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.VISIBILITY_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.VISIBILITY_OWNER -> MkplVisibility.VISIBLE_TO_OWNER
            SqlFields.VISIBILITY_GROUP -> MkplVisibility.VISIBLE_TO_GROUP
            SqlFields.VISIBILITY_PUBLIC -> MkplVisibility.VISIBLE_PUBLIC
            else -> MkplVisibility.NONE
        }
    },
    toDb = { value ->
        when (value) {
//            MkplVisibility.VISIBLE_TO_OWNER -> PGobject().apply { type = SqlFields.VISIBILITY_TYPE; value = SqlFields.VISIBILITY_OWNER}
            MkplVisibility.VISIBLE_TO_OWNER -> PgVisibilityOwner
            MkplVisibility.VISIBLE_TO_GROUP -> PgVisibilityGroup
            MkplVisibility.VISIBLE_PUBLIC -> PgVisibilityPublic
            MkplVisibility.NONE -> throw Exception("Wrong value of Visibility. NONE is unsupported")
        }
    }
)

sealed class PgVisibilityValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.VISIBILITY_TYPE
        value = eValue
    }
}

object PgVisibilityPublic: PgVisibilityValue(SqlFields.VISIBILITY_PUBLIC) {
    private fun readResolve(): Any = PgVisibilityPublic
}

object PgVisibilityOwner: PgVisibilityValue(SqlFields.VISIBILITY_OWNER) {
    private fun readResolve(): Any = PgVisibilityOwner
}

object PgVisibilityGroup: PgVisibilityValue(SqlFields.VISIBILITY_GROUP) {
    private fun readResolve(): Any = PgVisibilityGroup
}
