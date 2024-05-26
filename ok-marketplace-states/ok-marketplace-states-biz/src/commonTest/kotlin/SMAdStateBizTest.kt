package ru.otus.otuskotlin.marketplace.biz.statemachine

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.statemachine.resolver.SMAdStateResolverDefault
import ru.otus.otuskotlin.marketplace.states.common.MkplStateContext
import ru.otus.otuskotlin.marketplace.states.common.MkplStatesCorSettings
import ru.otus.otuskotlin.marketplace.states.common.models.*
import ru.otus.otuskotlin.marketplace.states.common.statemachine.SMAdSignal
import ru.otus.otuskotlin.marketplace.states.common.statemachine.SMAdStates
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days

class SMAdStateBizTest {

    @Test
    fun bizGetTest() = runTest {
        val machine = SMAdStateResolverDefault()
        val settings = MkplStatesCorSettings(stateMachine = machine)
        val processor = MkplAdStateProcessor(corSettings = settings)
        val ctx = MkplStateContext(
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.SUCCESS,
            stateRequest = MkplStateRq(
                adId = MkplAdStateId("some")
            )
        )
        processor.exec(ctx)
        assertEquals(SMAdStates.ACTUAL, ctx.stateResponse.oldState)
        assertContentEquals(emptyList(), ctx.errors)
        assertEquals(MkplState.FINISHING, ctx.state)
    }

    @Test
    fun new2hit() {
        val machine = SMAdStateResolverDefault()
        val signal = SMAdSignal(
            state = SMAdStates.NEW,
            duration = 2.days,
            views = 101,
        )
        val transition = machine.resolve(signal)
        assertEquals(SMAdStates.HIT, transition.state)
        assertContains(transition.description, "Очень", ignoreCase = true)
    }
}
