package me.bisspector.kustom.status

import me.bisspector.kustom.KustomServer
import me.bisspector.kustom.network.connection.PlayerConnection
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import java.util.*

class SimpleStatusResponseProvider(private val server: KustomServer, private val maxPlayers: Int) : StatusResponseProvider {

    override fun generateStatus(playerConnection: PlayerConnection): StatusResponse {
        return StatusResponse(
            ServerVersion(
                KustomServer.SERVER_INFO.GAME_VERSION,
                KustomServer.SERVER_INFO.PROTOCOL_VERSION
            ),
            PlayerList(
                maxPlayers,
                server.playerCount.get(),
                setOf(
                        "We're no strangers to love",
                        "You know the rules and so do I",
                        "A full commitment's what I'm thinking of",
                        "You wouldn't get this from any other guy",
                        "I just wanna tell you how I'm feeling",
                        "Gotta make you understand"
                    ).map { PlayerInfo(it, UUID.randomUUID())}.toSet()
            ),
            Component.text("kustom powered! connecting from ${playerConnection.channel.remoteAddress()}")
                .color(TextColor.color(0xFF00FF))
        )
    }
}