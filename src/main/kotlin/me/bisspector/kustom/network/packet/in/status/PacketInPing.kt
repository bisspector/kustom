package me.bisspector.kustom.network.packet.`in`.status

import io.netty.buffer.ByteBuf
import me.bisspector.kustom.network.packet.state.StatusPacket

class PacketInPing: StatusPacket(0x01) {
    var payload: Long = -1
    private set

    override fun read(buf: ByteBuf) {
        payload = buf.readLong()
    }
}