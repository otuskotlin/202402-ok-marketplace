package ru.otus.otuskotlin.marketplace.biz.validation

import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.biz.addTestPrincipal
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationDescriptionCorrect(command: MkplCommand, processor: MkplAdProcessor) = runBizTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adRequest = MkplAdStub.get(),
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkplState.FAILING, ctx.state)
    assertContains(ctx.adValidated.description, "болт")
}

fun validationDescriptionTrim(command: MkplCommand, processor: MkplAdProcessor) = runBizTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adRequest = MkplAdStub.prepareResult {
            description = " \n\tabc \n\t"
        },
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkplState.FAILING, ctx.state)
    assertEquals("abc", ctx.adValidated.description)
}

fun validationDescriptionEmpty(command: MkplCommand, processor: MkplAdProcessor) = runBizTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adRequest = MkplAdStub.prepareResult {
            description = ""
        },
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkplState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: MkplCommand, processor: MkplAdProcessor) = runBizTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adRequest = MkplAdStub.prepareResult {
            description = "!@#$%^&*(),.{}"
        },
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkplState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
