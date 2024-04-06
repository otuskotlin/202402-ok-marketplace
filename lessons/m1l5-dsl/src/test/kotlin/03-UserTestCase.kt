package ru.otus.otuskotlin.m1l5

import ru.otus.otuskotlin.m1l5.dsl.buildUser
import ru.otus.otuskotlin.m1l5.dsl.fri
import ru.otus.otuskotlin.m1l5.dsl.mon
import ru.otus.otuskotlin.m1l5.dsl.tomorrow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserTestCase {
    @Test
    fun `test user`() {
        val user = buildUser {
            name {
                first = "Kirill"
                last = "Krylov"
            }
            contacts {
                email = "email@gmail.com"
                phone = "81234567890"
            }
            actions {
                add(Action.UPDATE)
                add(Action.ADD)

                +Action.DELETE
                +Action.READ
            }
            availability {
                mon("11:30")
                fri("18:00")
                tomorrow("10:00")
            }
        }

        assertTrue(user.id.isNotEmpty())
        assertEquals("Kirill", user.firstName)
        assertEquals("", user.secondName)
        assertEquals("Krylov", user.lastName)
        assertEquals("email@gmail.com", user.email)
        assertEquals("81234567890", user.phone)
        assertEquals(setOf(Action.UPDATE, Action.ADD, Action.DELETE, Action.READ), user.actions)
        assertEquals(3, user.available.size)
    }
}
