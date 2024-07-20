package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.biz.addTestPrincipal
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdFilter
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateTitleHasContentTest {
    @Test
    fun emptyString() = runBizTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adValidating = MkplAd(title = ""))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runBizTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adValidating = MkplAd(title = "12!@#$%^&*()_+-="))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(MkplState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runBizTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdFilter(searchString = "Ж"))
        ctx.addTestPrincipal()
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
