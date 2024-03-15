package ru.otus.otuskotlin.oop

import org.junit.Test
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.test.assertEquals


internal class PropDelegationTest {
    @Test
    fun roDelegate() {
        val example = DelegateExample()

        println(example.constVal)
        assertEquals(example.constVal, 100501)
    }

    @Test
    fun rwDelegate() {
        val example = DelegateExample()
        example.varVal = 15
        println(example.varVal)
        assertEquals(example.varVal, 15)
    }

    @Test
    fun lazyDelegate() {
        val example = DelegateExample()
        println(example.lazyVal)
        assertEquals(example.lazyVal, 42)
    }

    private class ConstValue(private val value: Int) : ReadOnlyProperty<Any?, Int> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
            return value
        }
    }

    private class VarValue(private var value: Int) : ReadWriteProperty<Any?, Int> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
            return value
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            this.value = value
        }
    }

    private class DelegateExample {
        val constVal by ConstValue(100501)
        var varVal by VarValue(100501)
        val lazyVal by lazy {
            println("calculate...")
            42
        }
    }

}
