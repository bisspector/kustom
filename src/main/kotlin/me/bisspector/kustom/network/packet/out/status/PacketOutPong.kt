package me.bisspector.kustom.network.packet.out.status

import io.netty.buffer.ByteBuf
import me.bisspector.kustom.network.packet.state.StatusPacket

class PacketOutPong(val payload: Long): StatusPacket(0x01) {
    override fun write(buf: ByteBuf) {
        buf.writeLong(payload)
    }
}