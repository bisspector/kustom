package me.bisspector.kustom.network.packet.data

import me.bisspector.kustom.network.packet.state.PacketState
import java.net.InetAddress

data class HandshakeData(
    val protocolVersion: Int,
    val address: InetAddress,
    val port: Short,
    val nextState: PacketState
)
