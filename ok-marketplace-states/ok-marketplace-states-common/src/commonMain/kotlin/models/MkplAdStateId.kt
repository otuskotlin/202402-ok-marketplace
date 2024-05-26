package ru.otus.otuskotlin.marketplace.states.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MkplAdStateId(private val id: String) {
   fun asString() = id

   companion object {
       val NONE = MkplAdStateId("")
   }
}
