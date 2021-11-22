package me.bisspector.kustom.network.channel

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import me.bisspector.kustom.KustomServer
import me.bisspector.kustom.ext.logger
import me.bisspector.kustom.network.packet.Packet
import me.bisspector.kustom.network.connection.PlayerConnection
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

class ChannelHandler(private val server: KustomServer): SimpleChannelInboundHandler<Packet>() {
    lateinit var playerConnection: PlayerConnection

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        LOGGER.debug("[+] Channel connected: ${ctx.channel().remoteAddress()}")
        playerConnection = server.connectionManager.createPlayerConnection(ctx)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, packet: Packet) {
        playerConnection.packetHandler.handle(packet)
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        LOGGER.debug("[-] Channel disconnected: ${ctx.channel().remoteAddress()}")
        server.connectionManager.removePlayerConnection(ctx)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        LOGGER.error("Error occured", cause)
        server.connectionManager.getPlayerConnection(ctx)!!.disconnect(
            Component.text("Exception occurred in your connection!")
                .color(TextColor.fromHexString("#FF0000"))
        )
    }

    companion object {
        val LOGGER = logger<ChannelHandler>()

        const val NETTY_NAME = "channel_handler"
    }
}