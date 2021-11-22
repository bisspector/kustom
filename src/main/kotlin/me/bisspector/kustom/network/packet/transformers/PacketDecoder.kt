package me.bisspector.kustom.network.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import me.bisspector.kustom.KustomServer
import me.bisspector.kustom.ext.logger
import me.bisspector.kustom.network.channel.ChannelHandler
import me.bisspector.kustom.ext.readVarInt

class PacketDecoder: ByteToMessageDecoder() {
    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (buf.readableBytes() == 0) return

        val id = buf.readVarInt()
        val state = ctx.pipeline().get(ChannelHandler::class.java).playerConnection.state

        val packet = state.createPacket(id)
        if (packet == null) {
            LOGGER.debug("Packet was skipped [id: $id, state: $state]")
            buf.skipBytes(buf.readableBytes())
            return
        }

        LOGGER.debug("Incoming packet [id: $id, type: ${packet.javaClass}]")

        packet.read(buf)

        if (buf.readableBytes() != 0) {
            LOGGER.debug("More bytes from the packet $packet: ${buf.readableBytes()}")
        }

        out.add(packet)
    }

    companion object {
        val LOGGER = logger<PacketDecoder>()

        const val NETTY_NAME = "packet_decoder"
    }
}
