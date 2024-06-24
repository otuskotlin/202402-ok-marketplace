package ru.otus.otuskotlin.marketplace.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.marketplace.common.helpers.errorSystem
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.marketplace.common.repo.exceptions.RepoEmptyLockException
import ru.otus.otuskotlin.marketplace.repo.common.IRepoAdInitializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class AdRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : AdRepoBase(), IRepoAd, IRepoAdInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, AdEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(ads: Collection<MkplAd>) = ads.map { ad ->
        val entity = AdEntity(ad)
        require(entity.id != null)
        cache.put(entity.id, entity)
        ad
    }

    override suspend fun createAd(rq: DbAdRequest): IDbAdResponse = tryAdMethod {
        val key = randomUuid()
        val ad = rq.ad.copy(id = MkplAdId(key), lock = MkplAdLock(randomUuid()))
        val entity = AdEntity(ad)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbAdResponseOk(ad)
    }

    override suspend fun readAd(rq: DbAdIdRequest): IDbAdResponse = tryAdMethod {
        val key = rq.id.takeIf { it != MkplAdId.NONE }?.asString() ?: return@tryAdMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbAdResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateAd(rq: DbAdRequest): IDbAdResponse = tryAdMethod {
        val rqAd = rq.ad
        val id = rqAd.id.takeIf { it != MkplAdId.NONE } ?: return@tryAdMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqAd.lock.takeIf { it != MkplAdLock.NONE } ?: return@tryAdMethod errorEmptyLock(id)

        mutex.withLock {
            val oldAd = cache.get(key)?.toInternal()
            when {
                oldAd == null -> errorNotFound(id)
                oldAd.lock == MkplAdLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldAd.lock != oldLock -> errorRepoConcurrency(oldAd, oldLock)
                else -> {
                    val newAd = rqAd.copy(lock = MkplAdLock(randomUuid()))
                    val entity = AdEntity(newAd)
                    cache.put(key, entity)
                    DbAdResponseOk(newAd)
                }
            }
        }
    }


    override suspend fun deleteAd(rq: DbAdIdRequest): IDbAdResponse = tryAdMethod {
        val id = rq.id.takeIf { it != MkplAdId.NONE } ?: return@tryAdMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != MkplAdLock.NONE } ?: return@tryAdMethod errorEmptyLock(id)

        mutex.withLock {
            val oldAd = cache.get(key)?.toInternal()
            when {
                oldAd == null -> errorNotFound(id)
                oldAd.lock == MkplAdLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldAd.lock != oldLock -> errorRepoConcurrency(oldAd, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbAdResponseOk(oldAd)
                }
            }
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchAd(rq: DbAdFilterRequest): IDbAdsResponse = tryAdsMethod {
        val result: List<MkplAd> = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != MkplUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.dealSide.takeIf { it != MkplDealSide.NONE }?.let {
                    it.name == entry.value.adType
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbAdsResponseOk(result)
    }
}
