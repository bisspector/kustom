package me.bisspector.kustom.network.packet.handlers

import me.bisspector.kustom.KustomServer
import me.bisspector.kustom.network.connection.PlayerConnection
import me.bisspector.kustom.network.packet.Packet
import me.bisspector.kustom.network.packet.`in`.status.PacketInPing
import me.bisspector.kustom.network.packet.`in`.status.PacketInStatusRequest
import me.bisspector.kustom.network.packet.out.status.PacketOutPong
import me.bisspector.kustom.network.packet.out.status.PacketOutStatusRequest

class StatusHandler(
    override val server: KustomServer,
    override val connection: PlayerConnection
) : PacketHandler {
    override fun handle(packet: Packet) {
        when (packet) {
            is PacketInStatusRequest -> handleStatusRequest()
            is PacketInPing -> handlePing(packet)
            else -> return
        }
    }

    private fun handleStatusRequest() {
        connection.sendPacket(
            PacketOutStatusRequest(
                server.statusResponseProvider.generateStatus(connection)
            )
        )
    }

    private fun handlePing(packet: PacketInPing) {
        connection.sendPacket(
            PacketOutPong(packet.payload)
        )
    }
}
