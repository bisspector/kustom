package me.bisspector.kustom.network.packet.`in`.handshake

import io.netty.buffer.ByteBuf
import me.bisspector.kustom.network.packet.data.HandshakeData
import me.bisspector.kustom.network.packet.Packet
import me.bisspector.kustom.network.packet.PacketInfo
import me.bisspector.kustom.network.packet.state.PacketState
import me.bisspector.kustom.ext.readString
import me.bisspector.kustom.ext.readVarInt
import me.bisspector.kustom.ext.toInetAddress

class PacketInHandshake: Packet {
    override val info = PacketInfo(0x00, PacketState.HANDSHAKE)

    lateinit var data: HandshakeData
        private set

    override fun read(buf: ByteBuf) {
        data = HandshakeData(
            buf.readVarInt(),
            buf.readString(255).toInetAddress(),
            buf.readUnsignedShort().toShort(),
            PacketState.fromId(buf.readVarInt())
        )
    }
}