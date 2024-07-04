package ru.otus.otuskotlin.marketplace.backend.repo.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.annotations.*
import ru.otus.otuskotlin.marketplace.backend.repo.cassandra.model.AdCassandraDTO
import ru.otus.otuskotlin.marketplace.common.repo.DbAdFilterRequest
import java.util.concurrent.CompletionStage

@Dao
interface AdCassandraDAO {
    @Insert
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun create(dto: AdCassandraDTO): CompletionStage<Unit>

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun read(id: String): CompletionStage<AdCassandraDTO?>

    @Update(customIfClause = "lock = :prevLock")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun update(dto: AdCassandraDTO, prevLock: String): CompletionStage<AsyncResultSet>

    @Delete(customWhereClause = "id = :id", customIfClause = "lock = :prevLock", entityClass = [AdCassandraDTO::class])
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun delete(id: String, prevLock: String): CompletionStage<AsyncResultSet>

    @QueryProvider(providerClass = AdCassandraSearchProvider::class, entityHelpers = [AdCassandraDTO::class])
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun search(filter: DbAdFilterRequest): CompletionStage<Collection<AdCassandraDTO>>
}
