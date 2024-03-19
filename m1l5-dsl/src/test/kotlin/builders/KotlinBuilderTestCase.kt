@file:Suppress("unused")

package ru.otus.otuskotlin.m1l5.builders

import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinBuilderTestCase {
    @Test
    fun `test building java-like breakfast`() {
        val breakfast = breakfast {
            eggs = 3 // BreakfastBuilder().eggs = 3
            bacon = true
            title = "Simple"
            drink = Drink.COFFEE
        }

        assertEquals(3, breakfast.eggs)
        assertEquals(true, breakfast.bacon)
        assertEquals("Simple", breakfast.title)
        assertEquals(Drink.COFFEE, breakfast.drink)
    }

    enum class Drink {
        WATER,
        COFFEE,
        TEA,
        NONE,
    }

    abstract class Meal {
        data class Breakfast(
            val eggs: Int,
            val bacon: Boolean,
            val drink: Drink,
            val title: String,
        ) : Meal()

        data class Dinner(
            val title: String,
        ) : Meal()
    }

    private class KotlinLikeBuilder {
        var eggs = 0
        var bacon = false
        var drink = Drink.NONE
        var title = ""

        fun build() = Meal.Breakfast(eggs, bacon, drink, title)
    }

    private fun breakfast(block: KotlinLikeBuilder.() -> Unit): Meal.Breakfast {
        val builder = KotlinLikeBuilder()
        builder.block()
        return builder.build()
    }
}
