@file:Suppress("UNUSED_PARAMETER", "unused")

// calling JS code through a js function, awkward to maintain
fun useMathRound(value: Double) = js("Math.round(value)")

// using annotations for JS modules
@JsModule("is-sorted")
@JsNonModule
external fun <T> sorted(array: Array<T>): Boolean


// Using wrappers for JS modules.
//  Can be generated from TS using dukat (External tool).
@JsModule("js-big-decimal")
@JsNonModule
@JsName("BigDecimal")
external class JsBigDecimal(value: Any) {
    fun getValue(): String
    fun getPrettyValue(digits: Int, separator: String)
    fun round(precision: Int = definedExternally, mode: dynamic = definedExternally): JsBigDecimal

    companion object {
        fun getPrettyValue(number: Any, digits: Int, separator: String)
    }
}
