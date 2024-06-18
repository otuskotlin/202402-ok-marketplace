package ru.otus.otuskotlin.marketplace.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdFilter
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateTitleHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adValidating = MkplAd(title = ""))
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adValidating = MkplAd(title = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(MkplState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdFilter(searchString = "Ж"))
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateTitleHasContent("")
        }.build()
    }
}
