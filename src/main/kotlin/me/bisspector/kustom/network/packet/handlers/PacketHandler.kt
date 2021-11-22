package me.bisspector.kustom.network.packet.handlers

import me.bisspector.kustom.KustomServer
import me.bisspector.kustom.network.connection.PlayerConnection
import me.bisspector.kustom.network.packet.Packet

interface PacketHandler {
    val server: KustomServer
    val connection: PlayerConnection

    fun handle(packet: Packet)
}