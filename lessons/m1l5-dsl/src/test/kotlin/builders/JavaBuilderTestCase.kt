@file:Suppress("unused")

package ru.otus.otuskotlin.m1l5.builders

import kotlin.test.Test
import kotlin.test.assertEquals

class JavaBuilderTestCase {
    @Test
    fun `test building java-like breakfast`() {
        val breakfast = BreakfastBuilder()
            .withEggs(3)
            .withBacon(true)
            .withTitle("Simple")
            .withDrink(Drink.COFFEE)
            .build()

        assertEquals(3, breakfast.eggs)
        assertEquals(true, breakfast.bacon)
        assertEquals("Simple", breakfast.title)
        assertEquals(Drink.COFFEE, breakfast.drink)
    }

    private enum class Drink {
        WATER,
        COFFEE,
        TEA,
        NONE,
    }

    private abstract class Meal {
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

    private class BreakfastBuilder {
        var eggs = 0
        var bacon = false
        var drink = Drink.NONE
        var title = ""

        fun withEggs(eggs: Int): BreakfastBuilder {
            this.eggs = eggs
            return this
        }

        fun withBacon(bacon: Boolean): BreakfastBuilder {
            this.bacon = bacon
            return this
        }

        fun withDrink(drink: Drink): BreakfastBuilder {
            this.drink = drink
            return this
        }

        fun withTitle(title: String): BreakfastBuilder {
            this.title = title
            return this
        }

        fun build() = Meal.Breakfast(eggs, bacon, drink, title)
    }
}
