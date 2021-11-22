package me.bisspector.kustom.status

import me.bisspector.kustom.network.connection.PlayerConnection

fun interface StatusResponseProvider {
    fun generateStatus(playerConnection: PlayerConnection): StatusResponse
}
