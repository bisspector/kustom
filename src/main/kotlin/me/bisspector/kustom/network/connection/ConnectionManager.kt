package me.bisspector.kustom.network.connection

import io.netty.channel.ChannelHandlerContext
import me.bisspector.kustom.KustomServer
import java.util.concurrent.ConcurrentHashMap

class ConnectionManager(private val server: KustomServer) {
    private val connectionHashMap = ConcurrentHashMap<ChannelHandlerContext, PlayerConnection>()

    fun createPlayerConnection(ctx: ChannelHandlerContext): PlayerConnection {
        val playerConnection = PlayerConnection(server, ctx.channel())
        connectionHashMap[ctx] = playerConnection
        return playerConnection
    }

    fun getPlayerConnection(ctx: ChannelHandlerContext): PlayerConnection? {
        return connectionHashMap[ctx]
    }

    fun removePlayerConnection(ctx: ChannelHandlerContext): PlayerConnection? {
        return connectionHashMap.remove(ctx)
    }
}
