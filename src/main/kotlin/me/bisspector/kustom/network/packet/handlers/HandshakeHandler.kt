package me.bisspector.kustom.network.packet.handlers

import me.bisspector.kustom.KustomServer
import me.bisspector.kustom.network.connection.PlayerConnection
import me.bisspector.kustom.network.packet.Packet
import me.bisspector.kustom.network.packet.`in`.handshake.PacketInHandshake
import me.bisspector.kustom.network.packet.state.PacketState
import java.lang.UnsupportedOperationException

class HandshakeHandler(
    override val server: KustomServer,
    override val connection: PlayerConnection
) : PacketHandler {

    override fun handle(packet: Packet) {
        if (packet !is PacketInHandshake) return
        when (val nextState = packet.data.nextState) {
            PacketState.STATUS -> handleStatus()
            PacketState.LOGIN -> handleLogin()
            else -> throw UnsupportedOperationException("Illegal next state $nextState")
        }
    }

    private fun handleStatus() {
        connection.state = PacketState.STATUS
        connection.packetHandler = StatusHandler(server, connection)
    }

    private fun handleLogin() {
        connection.state = PacketState.LOGIN
        connection.packetHandler = LoginHandler(server, connection)
        return
    }
}
