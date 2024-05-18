package ru.otus.otuskotlin.kmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class UserLongService() {
    suspend fun serve(user: User): Pair<String, User>
}
