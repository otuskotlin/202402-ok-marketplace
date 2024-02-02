package ru.otus.otuskotlin.kmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class UserService() {
    fun serve(user: User): String
}
