package ru.otus.otuskotlin.interop

class RustExample {
    external fun rust_add(a: Int, b: Int): Int

    init {
        System.loadLibrary("c_jni")
    }
}
