@file:OptIn(ExperimentalForeignApi::class)

import kotlinx.cinterop.*
import libcurl.MySumStruct
import libcurl.sum_fun
import libcurl.sum_ref
import libcurl.sum_struct

// Использование структур в c-функциях
fun sumStruct(a: Int, b: Double): Double {

    // Инициализируем экземпляр c-структуры
    val s = cValue<MySumStruct> {
        this.a = a
        this.b = b
    }
    // передаем в функцию саму структуру
    return sum_struct(s)
}

// Использование c-ссылок на структуры в Котлин
fun sumRef(a: Int, b: Double): Double = cValue<MySumStruct> {
    this.a = a
    this.b = b
}.usePinned { // фиксируем положение структуры в памяти на время вызова
    // передаем в c-функцию ссылку на структуру
    sum_ref(it.get())
}

// Аллоцирование памяти для использования в c-функциях
fun sumAlloc(a: Int, b: Double): Double = memScoped {
    // Это блок со скоупом памяти. По выходу из этого скоупа, память автоматически очистится
    // Это избавляет нас от явного освобождения памяти через free(mem)

    // Выделяем динамическую память
    alloc<MySumStruct> {
        this.a = a
        this.b = b
    }.usePinned {// фиксируем положение структуры в памяти на время вызова
        // вызываем функцию с сылкой на аллоцированную структуру
        sum_ref(it.get().ptr)
    }
}

// Аллоцирование памяти для использования в c-функциях
fun sumCallback(a: Int, b: Double): Double {
    return cValue<MySumStruct> {
        this.a = a
        this.b = b
    }.usePinned { // фиксируем положение структуры в памяти на время вызова

        // Для демонстрации non-capturing природы колбэков для СИ
        @Suppress("UNUSED_VARIABLE") val capturingVariable = a.toString()

        // передаем в c-функцию специально подготовленную функцию
        sum_fun(it.get(), staticCFunction { a: Int, b: Double ->
//            println("Эта строка не сработает: функция должна быть non-capturing, т.е. не ссылаться наружу -> $capturingVariable")
            a.toDouble() + b
        })
    }
}
