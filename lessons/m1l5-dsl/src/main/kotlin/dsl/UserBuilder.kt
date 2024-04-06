package ru.otus.otuskotlin.m1l5.dsl

import ru.otus.otuskotlin.m1l5.Action
import ru.otus.otuskotlin.m1l5.User
import java.time.LocalDateTime
import java.util.*

@UserDsl
class UserBuilder {
    private var id = UUID.randomUUID().toString()

    private var firstName = ""
    private var secondName = ""
    private var lastName = ""

    private var phone = ""
    private var email = ""

    private var actions = emptySet<Action>()

    private var available = emptyList<LocalDateTime>()

    fun name(block: NameContext.() -> Unit) {
        val ctx = NameContext().apply(block)

        firstName = ctx.first
        secondName = ctx.second
        lastName = ctx.last
    }

    fun contacts(block: ContactsContext.() -> Unit) {
        val ctx = ContactsContext().apply(block)

        phone = ctx.phone
        email = ctx.email
    }

    fun actions(block: ActionsContext.() -> Unit) {
        val ctx = ActionsContext().apply(block)

        actions = ctx.build()
    }

    fun availability(block: AvailabilityContext.() -> Unit) {
        val ctx = AvailabilityContext().apply(block)

        available = ctx.availabilities
    }

    fun build() = User(
        id = id,
        firstName = firstName,
        secondName = secondName,
        lastName = lastName,
        phone = phone,
        email = email,
        actions = actions,
        available = available,
    )
}
