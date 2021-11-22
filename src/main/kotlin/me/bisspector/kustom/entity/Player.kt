package me.bisspector.kustom.entity

import me.bisspector.kustom.KustomServer
import me.bisspector.kustom.event.PlayerDisconnectEvent
import me.bisspector.kustom.network.connection.PlayerConnection
import net.kyori.adventure.text.Component
import java.util.*

class Player(
    private val server: KustomServer,
    private val connection: PlayerConnection,
    val uuid: UUID,
    val username: String
) {
    fun kick(reason: Component = Component.text("Disconnected!")) {
        val event = PlayerDisconnectEvent(this)
        server.globalEventHandler.call(event)
        connection.disconnect(reason)
    }
}
