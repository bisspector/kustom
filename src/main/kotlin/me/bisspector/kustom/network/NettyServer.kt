package me.bisspector.kustom.network

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.kqueue.KQueue
import io.netty.channel.kqueue.KQueueEventLoopGroup
import io.netty.channel.kqueue.KQueueServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.ServerSocketChannel
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.bisspector.kustom.KustomServer
import me.bisspector.kustom.concurrency.NamedThreadFactory
import me.bisspector.kustom.ext.logger
import me.bisspector.kustom.network.channel.ChannelHandler
import me.bisspector.kustom.network.packet.transformers.*
import java.io.IOException
import kotlin.system.exitProcess


class NettyServer(private val server: KustomServer) {
    private val bossGroup: EventLoopGroup = bestLoopGroup()
    private val workerGroup: EventLoopGroup = bestLoopGroup()
    private val channel = bestChannel()

    suspend fun run(host: String, port: Short) {
        LOGGER.debug("${bossGroup::class.simpleName} is the best event loop group")

        try {
            val bootstrap = ServerBootstrap()
            bootstrap.group(bossGroup, workerGroup)
                .channel(channel)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline()
                            .addLast("timeout", ReadTimeoutHandler(30))
//                            .addLast(LegacyPingHandler.NETTY_NAME, LegacyPingHandler(server))
                            .addLast(SizeDecoder.NETTY_NAME, SizeDecoder())
                            .addLast(PacketDecoder.NETTY_NAME, PacketDecoder())
                            .addLast(SizeEncoder.NETTY_NAME, SizeEncoder())
                            .addLast(PacketEncoder.NETTY_NAME, PacketEncoder())
                            .addLast(ChannelHandler.NETTY_NAME, ChannelHandler(server))
                    }
                })

            withContext(Dispatchers.IO) {
                val future = bootstrap.bind(host, port.toInt()).syncUninterruptibly()
                future.channel().closeFuture().syncUninterruptibly()
            }
        } catch (exception: IOException) {
            LOGGER.error("Failed to bind port $port!")
            LOGGER.error("Exception: $exception")
            exitProcess(0)
        } finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }

    private fun bestLoopGroup() = when {
        Epoll.isAvailable() -> EpollEventLoopGroup(0, NamedThreadFactory("Netty Epoll Worker #%d"))
        KQueue.isAvailable() -> KQueueEventLoopGroup(0, NamedThreadFactory("Netty KQueue Worker #%d"))
        else -> NioEventLoopGroup(0, NamedThreadFactory("Netty NIO Worker #%d"))
    }

    private fun bestChannel(): Class<out ServerSocketChannel> = when {
        Epoll.isAvailable() -> EpollServerSocketChannel::class.java
        KQueue.isAvailable() -> KQueueServerSocketChannel::class.java
        else -> NioServerSocketChannel::class.java
    }

    companion object {
        private val LOGGER = logger<NettyServer>()
    }
}
