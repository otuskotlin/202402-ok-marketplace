package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import ru.otus.otuskotlin.marketplace.common.repo.*

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class RepoAdSql(
    properties: SqlProperties,
    randomUuid: () -> String = { uuid4().toString() },
) : IRepoAd {
    override suspend fun createAd(rq: DbAdRequest): IDbAdResponse
    override suspend fun readAd(rq: DbAdIdRequest): IDbAdResponse
    override suspend fun updateAd(rq: DbAdRequest): IDbAdResponse
    override suspend fun deleteAd(rq: DbAdIdRequest): IDbAdResponse
    override suspend fun searchAd(rq: DbAdFilterRequest): IDbAdsResponse
}
