package me.bisspector.kustom.network.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import me.bisspector.kustom.ext.logger
import me.bisspector.kustom.network.packet.Packet
import me.bisspector.kustom.ext.writeVarInt

class PacketEncoder: MessageToByteEncoder<Packet>() {
    override fun encode(ctx: ChannelHandlerContext, packet: Packet, out: ByteBuf) {
        LOGGER.debug("Outgoing packet [id: ${packet.info.id}, type: ${packet.javaClass}]")
        out.writeVarInt(packet.info.id)
        packet.write(out)
    }

    companion object {
        val LOGGER = logger<PacketEncoder>()

        const val NETTY_NAME = "packet_encoder"
    }
}
