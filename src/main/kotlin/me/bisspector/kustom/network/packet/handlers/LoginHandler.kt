package me.bisspector.kustom.network.packet.handlers

import me.bisspector.kustom.KustomServer
import me.bisspector.kustom.network.connection.PlayerConnection
import me.bisspector.kustom.network.packet.Packet
import me.bisspector.kustom.network.packet.`in`.login.PacketInLoginStart

class LoginHandler(
    override val server: KustomServer,
    override val connection: PlayerConnection
) : PacketHandler {
    override fun handle(packet: Packet) {
        when (packet) {
            is PacketInLoginStart -> connection.beginPlayState(packet.name)
        }
    }
}