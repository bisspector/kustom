package me.bisspector.kustom.network.packet.transformers

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import me.bisspector.kustom.KustomServer
import me.bisspector.kustom.ext.logger
import me.bisspector.kustom.ext.readAvailableBytes
import java.net.InetSocketAddress

//class LegacyPingHandler(private val server: KustomServer) : ChannelInboundHandlerAdapter() {
//    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
//        val buf = (msg as ByteBuf).copy()
//        buf.markReaderIndex()
//
//        var failed = true
//        try {
//            if (buf.readUnsignedByte() != 254.toShort()) return
//
//            val statusConfig = server.config.status
//            val motd = statusConfig.motd.content()
//            val maxPlayers = statusConfig.maxPlayers
//            val playerCount = server.playerCount.get()
//            val address = ctx.channel().remoteAddress() as InetSocketAddress
//            val version = KustomServer.SERVER_INFO.GAME_VERSION
//
//            when (buf.readableBytes()) {
//                0 -> {
//                    LOGGER.debug("Legacy server list ping (versions <=1.3.x) received from ${address.address}:${address.port}")
//                    ctx.sendFlushAndClose("$motd\u00a7$playerCount\u00a7$maxPlayers".toReply())
//                }
//                1 -> {
//                    if (buf.readUnsignedByte() != 1.toShort()) return
//                    LOGGER.debug("Legacy server list ping (versions 1.4.x-1.5.x) received from ${address.address}:${address.port}")
//                    ctx.sendFlushAndClose("\u00a71\u0000127\u0000$version\u0000$motd\u0000$playerCount\u0000$maxPlayers".toReply())
//                }
//                else -> {
//                    var isValid = buf.readUnsignedByte() == 1.toShort()
//                    isValid = isValid and (buf.readUnsignedByte() == 250.toShort())
//                    isValid = isValid and ("MC|PingHost" == String(
//                        buf.readAvailableBytes(buf.readShort() * 2),
//                        Charsets.UTF_16BE
//                    ))
//                    val dataLength = buf.readUnsignedShort()
//                    isValid = isValid and (buf.readUnsignedByte() >= 73)
//                    isValid = isValid and (3 + buf.readAvailableBytes(buf.readShort() * 2).size + 4 == dataLength)
//                    isValid = isValid and (buf.readInt() <= 65535)
//                    isValid = isValid and (buf.readableBytes() == 0)
//                    if (!isValid) return
//
//                    LOGGER.debug("Legacy server list ping (version 1.6.x) received from ${address.address}:${address.port}")
//                    ctx.sendFlushAndClose("\u00a71\u0000127\u0000$version\u0000$motd\u0000$playerCount\u0000$maxPlayers".toReply())
//                }
//            }
//            buf.release()
//            failed = false
//        } finally {
//            if (failed) {
//                buf.resetReaderIndex()
//                ctx.channel().pipeline().remove(NETTY_NAME)
//                ctx.fireChannelRead(msg)
//            }
//        }
//    }
//
//    private fun ChannelHandlerContext.sendFlushAndClose(buf: ByteBuf) {
//        pipeline().firstContext().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE)
//    }
//
//    private fun String.toReply(): ByteBuf {
//        val buf = Unpooled.buffer()
//        buf.writeByte(255)
//        val chars = toCharArray()
//        buf.writeShort(chars.size)
//        for (char in chars) buf.writeChar(char.toInt())
//        return buf
//    }
//    companion object {
//        const val NETTY_NAME = "legacy_query"
//
//        val LOGGER = logger<LegacyPingHandler>()
//    }
//}