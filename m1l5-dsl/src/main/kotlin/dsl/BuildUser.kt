package ru.otus.otuskotlin.m1l5.dsl

fun buildUser(block: UserBuilder.() -> Unit) = UserBuilder().apply(block).build()
