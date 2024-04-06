@file:OptIn(ExperimentalJsExport::class)

@JsExport
fun fromKotlin() {
    println("From Kotlin function")
}

@JsExport
data class FromKotlinClass(
    val some: String = "",
    val id: Int = 0,
)

@JsExport
val jsonApp = kotlin.js.json(Pair("name", "App1"))
