package ru.otus.otuskotlin.marketplace.logging.socket

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.marketplace.logging.common.IMpLogWrapper
import ru.otus.otuskotlin.marketplace.logging.common.LogLevel

@ExperimentalStdlibApi
class MpLoggerWrapperSocket(
    override val loggerId: String,
    private val host: String = "127.0.0.1",
    private val port: Int = 9002,
    private val emitToStdout: Boolean = true,
    bufferSize: Int = 16,
    overflowPolicy: BufferOverflow = BufferOverflow.SUSPEND,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default + CoroutineName("Logging")),
) : IMpLogWrapper {
    private val selectorManager = SelectorManager(Dispatchers.IO)
    private val sf = MutableSharedFlow<LogData>(
        extraBufferCapacity = bufferSize,
        onBufferOverflow = overflowPolicy,
    )
    private val isActive: AtomicBoolean = atomic(true)
    val isReady: AtomicBoolean = atomic(false)
    private val jsonSerializer = Json {
        encodeDefaults = true
    }

    private val job = scope.launch { handleLogs() }

    private suspend fun handleLogs() {
        while (isActive.value) {
            try {

                aSocket(selectorManager).tcp().connect(host, port).use { socket ->
                    socket.openWriteChannel().use {
                        sf
                            .onSubscription { isReady.value = true }
                            .collect {
                                val json = jsonSerializer.encodeToString(LogData.serializer(), it)
                                if (emitToStdout) println(json)
                                writeStringUtf8(json + "\n")
                                flush()
                            }
                    }
                }
            } catch (e: Throwable) {
                println("Error connecting log socket: $e")
                e.printStackTrace()
                delay(300)
            }
        }
    }

    override fun log(
        msg: String,
        level: LogLevel,
        marker: String,
        e: Throwable?,
        data: Any?,
        objs: Map<String, Any>?
    ) {
        // Это костыль!
        // Здесь происходит блокировка потока.
        // Решается либо превращением в корутину, либо использованием tryEmit(): Boolean
        runBlocking {
            sf.emit(
                LogData(
                    level = level,
                    message = msg,
                )
            )
        }
    }

    override fun close() {
        isActive.value = false
        isReady.value = false
        job.cancel(message = "Finishing")
    }
}
