package me.bisspector.kustom.network.packet.`in`.login

import io.netty.buffer.ByteBuf
import me.bisspector.kustom.ext.readString
import me.bisspector.kustom.network.packet.state.LoginPacket

class PacketInLoginStart: LoginPacket(0x00) {
    lateinit var name: String
    private set

    override fun read(buf: ByteBuf) {
        name = buf.readString(16)
    }
}