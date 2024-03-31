package ru.otus.otuskotlin.kmp

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserAsyncServiceTest {

    @Test
    fun coroutine() = runTest {
        val user = User("1", "Ivan", 24)
        val service = UserAsyncService()
        val res = service.serve(user)
        assertEquals(res.second, user)
        assertTrue {
            res.first == "JS"
                    || res.first == "JVM"
                    || res.first == "Native"
        }
    }
}
