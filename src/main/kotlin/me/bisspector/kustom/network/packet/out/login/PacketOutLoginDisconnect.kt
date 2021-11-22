package me.bisspector.kustom.network.packet.out.login

import io.netty.buffer.ByteBuf
import me.bisspector.kustom.ext.writeChat
import me.bisspector.kustom.network.packet.state.LoginPacket
import net.kyori.adventure.text.Component

class PacketOutLoginDisconnect(private val reason: Component) : LoginPacket(0x00) {
    override fun write(buf: ByteBuf) {
        buf.writeChat(reason)
    }
}