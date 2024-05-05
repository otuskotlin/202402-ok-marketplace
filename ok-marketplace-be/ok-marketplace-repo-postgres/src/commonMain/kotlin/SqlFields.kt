package ru.otus.otuskotlin.marketplace.backend.repo.postgresql

object SqlFields {
    val ID = "id"
    val TITLE = "title"
    val DESCRIPTION = "description"
    val AD_TYPE = "ad_type"
    val VISIBILITY = "visibility"
    val LOCK = "lock"
    val OWNER_ID = "owner_id"
    val PRODUCT_ID = "product_id"

    val AD_TYPE_DEMAND = "demand"
    val AD_TYPE_SUPPLY = "supply"

    val VISIBILITY_PUBLIC = "public"
    val VISIBILITY_OWNER = "owner"
    val VISIBILITY_GROUP = "group"

    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        ID, TITLE, DESCRIPTION, AD_TYPE, VISIBILITY, LOCK, OWNER_ID, PRODUCT_ID,
    )
}
