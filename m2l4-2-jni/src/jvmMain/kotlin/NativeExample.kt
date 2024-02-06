package ru.otus.otuskotlin.interop

class NativeExample {
    external fun native_add(a: Int, b: Int): Int

    init {
        System.loadLibrary("c_jni")
    }
}
