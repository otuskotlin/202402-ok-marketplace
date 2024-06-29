package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.marketplace.repo.common.IRepoAdInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoAdSql actual constructor(
    properties: SqlProperties,
    randomUuid: () -> String
) : IRepoAd, IRepoAdInitializable {
    actual override fun save(ads: Collection<MkplAd>): Collection<MkplAd> {
        TODO("Not yet implemented")
    }

    actual override suspend fun createAd(rq: DbAdRequest): IDbAdResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun readAd(rq: DbAdIdRequest): IDbAdResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun updateAd(rq: DbAdRequest): IDbAdResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun deleteAd(rq: DbAdIdRequest): IDbAdResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun searchAd(rq: DbAdFilterRequest): IDbAdsResponse {
        TODO("Not yet implemented")
    }

    actual fun clear() {
        TODO("Not yet implemented")
    }
}
