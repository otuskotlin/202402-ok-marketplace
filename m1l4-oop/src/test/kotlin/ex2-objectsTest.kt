package ru.otus.otuskotlin.oop

import org.junit.Test

class ObjectsExample {
    companion object {
        init {
            println("companion inited") // init when ObjectsExample will be loaded
        }

        fun doSmth() {
            println("companion object")
        }
    }

    object A {
        init {
            println("A inited") // lazy init whet getting A first time
        }

        fun doSmth() {
            println("object A")
        }
    }
}

class ObjectsTest {
    @Test
    fun test() {
        ObjectsExample()
        ObjectsExample.doSmth()
        ObjectsExample.A.doSmth()
        ObjectsExample.A.doSmth()
    }
}
