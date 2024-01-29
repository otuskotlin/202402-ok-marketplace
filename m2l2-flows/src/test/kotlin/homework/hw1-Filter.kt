package ru.otus.otuskotlin.flows.homework

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import kotlin.test.Test

/**
 * Задание.
 * Добавить необходимые фильтры для того, чтоб тесты заработали как надо.
 *
 * Описание. У нас БД в памяти. В ней нужно найти объект, описанный фильтром SearchFilter.
 */
class Exercise1Filter {
    @Test
    fun filter() = runBlocking {
        val flt = SearchFilter(
            title = "шнурки",
            type = AdType.DEMAND,
            visibilitiesOr = setOf(AdVisibility.OWNER, AdVisibility.GROUP),
            priceMin = BigDecimal("10.00"),
        )
        val res = LIST
            .asFlow()
            .run { flt.title?.let { t -> this.filter { it.title == t } } ?: this }
            .toList()
//        assertEquals(1, res.size)
//        assertEquals("5", res.first().id)
    }

    companion object {
        data class SearchFilter(
            val title: String? = null,
            val visibilitiesOr: Set<AdVisibility>? = null,
            val priceMin: BigDecimal? = null,
            val priceMax: BigDecimal? = null,
            val type: AdType? = null,
        )

        data class Ad(
            val id: String,
            val title: String,
            val visibility: AdVisibility,
            val price: BigDecimal,
            val type: AdType,
        )

        enum class AdVisibility { PUBLIC, GROUP, OWNER }
        enum class AdType { DEMAND, SUPPLY }

        val LIST = listOf(
            Ad("1", "носок", AdVisibility.PUBLIC, BigDecimal("22.13"), AdType.SUPPLY),
            Ad("2", "носок", AdVisibility.PUBLIC, BigDecimal("22.13"), AdType.DEMAND),
            Ad("3", "носок", AdVisibility.PUBLIC, BigDecimal("40.13"), AdType.DEMAND),
            Ad("4", "носок", AdVisibility.OWNER, BigDecimal("40.13"), AdType.DEMAND),
            Ad("5", "шнурки", AdVisibility.OWNER, BigDecimal("40.13"), AdType.DEMAND),
            Ad("6", "шнурки", AdVisibility.OWNER, BigDecimal("40.13"), AdType.SUPPLY),
            Ad("7", "шнурки", AdVisibility.GROUP, BigDecimal("40.13"), AdType.DEMAND),
        )
    }
}

