package me.bisspector.kustom.network.packet.out.login

import io.netty.buffer.ByteBuf
import me.bisspector.kustom.ext.writeString
import me.bisspector.kustom.ext.writeUUID
import me.bisspector.kustom.network.packet.state.LoginPacket
import java.util.*

class PacketOutLoginSuccess(private val uuid: UUID, private val username: String) : LoginPacket(0x02) {
    override fun write(buf: ByteBuf) {
        buf.writeUUID(uuid)
        buf.writeString(username)
    }
}
