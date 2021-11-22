package me.bisspector.kustom.network.packet.`in`.status

import io.netty.buffer.ByteBuf
import me.bisspector.kustom.network.packet.state.StatusPacket

class PacketInStatusRequest: StatusPacket(0x0) {
    override fun read(buf: ByteBuf) {
        // Packet is empty
    }
}