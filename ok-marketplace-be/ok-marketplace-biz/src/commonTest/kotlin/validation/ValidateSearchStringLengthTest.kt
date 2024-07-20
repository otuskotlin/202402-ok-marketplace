package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.biz.addTestPrincipal
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplAdFilter
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runBizTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runBizTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdFilter(searchString = "  "))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runBizTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdFilter(searchString = "12"))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(MkplState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runBizTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdFilter(searchString = "123"))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runBizTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdFilter(searchString = "12".repeat(51)))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(MkplState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
