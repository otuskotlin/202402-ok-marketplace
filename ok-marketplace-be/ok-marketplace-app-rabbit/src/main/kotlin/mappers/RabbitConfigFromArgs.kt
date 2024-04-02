package ru.otus.otuskotlin.marketplace.app.rabbit.mappers

import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitConfig

fun RabbitConfig.Companion.fromArgs(vararg args: String) = RabbitConfig(
    host = args.arg("-h") ?: HOST,
    port = args.arg("-p")?.toInt() ?: PORT,
    user = args.arg("-u") ?: USER,
    password = args.arg("-pw") ?: PASSWORD,
)

private fun Array<out String>.arg(option: String) = indexOf(option)
    .takeIf { it != -1 }
    ?.let { this[it + 1] }
