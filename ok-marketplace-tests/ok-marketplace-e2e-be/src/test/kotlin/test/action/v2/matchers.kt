package ru.otus.otuskotlin.marketplace.e2e.be.test.action.v2

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.and
import ru.otus.otuskotlin.marketplace.api.v2.models.*


fun haveResult(result: ResponseResult) = Matcher<IResponse> {
    MatcherResult(
        it.result == result,
        { "actual result ${it.result} but we expected $result" },
        { "result should not be $result" }
    )
}

val haveNoErrors = Matcher<IResponse> {
    MatcherResult(
        it.errors.isNullOrEmpty(),
        { "actual errors ${it.errors} but we expected no errors" },
        { "errors should not be empty" }
    )
}

//fun haveError(code: String) = haveResult(ResponseResult.ERROR)
//    .and(Matcher<IResponse> {
//        MatcherResult(
//            it.errors?.firstOrNull { e -> e.code == code } != null,
//            { "actual errors ${it.errors} but we expected error with code $code" },
//            { "errors should not contain $code" }
//        )
//    })

val haveSuccessResult = haveResult(ResponseResult.SUCCESS) and haveNoErrors

val IResponse.ad: AdResponseObject?
    get() = when (this) {
        is AdCreateResponse -> ad
        is AdReadResponse -> ad
        is AdUpdateResponse -> ad
        is AdDeleteResponse -> ad
        is AdOffersResponse -> ad
        else -> throw IllegalArgumentException("Invalid response type: ${this::class}")
    }
