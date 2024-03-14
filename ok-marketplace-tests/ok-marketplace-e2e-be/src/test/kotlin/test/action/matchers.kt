package ru.otus.otuskotlin.marketplace.blackbox.test.action

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult

val beValidId = Matcher<String?> {
    MatcherResult(
        it != null,
        { "id should not be null" },
        { "id should be null" },
    )
}

val beValidLock = Matcher<String?> {
    MatcherResult(
        true, // TODO заменить на it != null, когда заработают локи
        { "lock should not be null" },
        { "lock should be null" },
    )
}

