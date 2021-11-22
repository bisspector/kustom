package me.bisspector.kustom

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.bisspector.kustom.entity.Player
import me.bisspector.kustom.event.*
import me.bisspector.kustom.ext.logger
import me.bisspector.kustom.instance.InstanceManager
import me.bisspector.kustom.network.NettyServer
import me.bisspector.kustom.network.connection.ConnectionManager
import me.bisspector.kustom.status.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass
import kotlin.system.measureTimeMillis

class KustomServer {

    // Network
    private val backendServer = NettyServer(this)
    val connectionManager = ConnectionManager(this)
    var statusResponseProvider: StatusResponseProvider =
        SimpleStatusResponseProvider(this, 100)

    // Handler that catches all events
    val globalEventHandler = GlobalEventHandler()

    // Ticking
    val tickrate = 20
    val tickInterval = 1000L / tickrate
    private val tickScheduler = Executors.newSingleThreadScheduledExecutor()
    private var lastTickTime = 0L
    private var tickCount = 0

    // Gameplay
    val playerCount = AtomicInteger(0)
    val players = mutableSetOf<Player>()
    val instanceManager = InstanceManager()

    var started = false
    private set

    fun start(host: String, port: Short): KustomServer {
        if (started) {
            LOGGER.error("Already started the server!")
            return this
        }
        val startupTime = System.currentTimeMillis()
        LOGGER.info("Server starting up...")

        LOGGER.debug("Starting netty...")
        GlobalScope.launch(Dispatchers.IO) {
            backendServer.run(host, port)
        }

        lastTickTime = System.currentTimeMillis()
        tickScheduler.scheduleAtFixedRate({
            val lastTickDuration = System.currentTimeMillis() - lastTickTime
            if (lastTickDuration > 2000L) {
                LOGGER.warn("Can't keep up! Running ${lastTickDuration}ms (${lastTickDuration / 50} ticks) behind!")
            }
            val tickDuration = measureTimeMillis(::tick)
            val finishTime = System.currentTimeMillis()
            lastTickTime = finishTime
        }, 0, tickInterval, TimeUnit.MILLISECONDS)

        started = true
        LOGGER.info("Server successfully started in ${System.currentTimeMillis() - startupTime}ms")

        return this
    }

    private fun tick() {
        tickCount++
        instanceManager.tick()
    }

    companion object {
        val SERVER_INFO = ServerInfo(
            "1.16.5",
            754
        )

        val LOGGER = logger<KustomServer>()
    }

    data class ServerInfo(
        val GAME_VERSION: String,
        val PROTOCOL_VERSION: Int
    )

}