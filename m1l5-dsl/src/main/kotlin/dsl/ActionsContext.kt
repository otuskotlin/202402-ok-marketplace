package ru.otus.otuskotlin.m1l5.dsl

import ru.otus.otuskotlin.m1l5.Action


@UserDsl
class ActionsContext {
    private val _actions: MutableSet<Action> = mutableSetOf()

    fun build(): Set<Action> = _actions.toSet()

    fun add(action: Action) = _actions.add(action)

    @Suppress("MemberVisibilityCanBePrivate")
    fun add(value: String) = add(Action.valueOf(value))

    operator fun Action.unaryPlus() = add(this)

    operator fun String.unaryPlus() = add(this)
}
