package me.bisspector.kustom.network.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import me.bisspector.kustom.ext.varIntSize
import me.bisspector.kustom.ext.writeVarInt

class SizeEncoder: MessageToByteEncoder<ByteBuf>() {
    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        val packetSize = msg.readableBytes()
        out.ensureWritable(packetSize.varIntSize() + packetSize)
        out.writeVarInt(packetSize)
        out.writeBytes(msg)
    }

    companion object {
        const val NETTY_NAME = "size_prepender"
    }
}