package me.bisspector.kustom.network.packet.state

import me.bisspector.kustom.network.packet.Packet
import me.bisspector.kustom.network.packet.PacketInfo

abstract class StatusPacket(id: Int) : Packet {
    override val info = PacketInfo(id, PacketState.STATUS)
}