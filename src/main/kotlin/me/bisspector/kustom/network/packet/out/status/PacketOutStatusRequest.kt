package me.bisspector.kustom.network.packet.out.status

import io.netty.buffer.ByteBuf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.bisspector.kustom.ext.writeString
import me.bisspector.kustom.status.StatusResponse
import me.bisspector.kustom.network.packet.state.StatusPacket

class PacketOutStatusRequest(private val status: StatusResponse) : StatusPacket(0x0) {
    override fun write(buf: ByteBuf) {
        buf.writeString(Json.encodeToString(status))
    }
}