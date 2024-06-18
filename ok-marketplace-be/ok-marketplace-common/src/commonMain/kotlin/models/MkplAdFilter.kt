package ru.otus.otuskotlin.marketplace.common.models

data class MkplAdFilter(
    var searchString: String = "",
    var ownerId: MkplUserId = MkplUserId.NONE,
    var dealSide: MkplDealSide = MkplDealSide.NONE,
) {
    fun deepCopy(): MkplAdFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MkplAdFilter()
    }
}
