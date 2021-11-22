package me.bisspector.kustom.network.connection

import io.netty.channel.Channel
import me.bisspector.kustom.KustomServer
import me.bisspector.kustom.entity.Player
import me.bisspector.kustom.event.PlayerLoginEvent
import me.bisspector.kustom.network.packet.Packet
import me.bisspector.kustom.network.packet.handlers.HandshakeHandler
import me.bisspector.kustom.network.packet.handlers.PacketHandler
import me.bisspector.kustom.network.packet.out.login.PacketOutLoginDisconnect
import me.bisspector.kustom.network.packet.out.login.PacketOutLoginSuccess
import me.bisspector.kustom.network.packet.state.PacketState
import net.kyori.adventure.text.Component
import java.util.*

class PlayerConnection(
    private val server: KustomServer,
    val channel: Channel
) {
    var packetHandler: PacketHandler = HandshakeHandler(server, this)
    var state = PacketState.HANDSHAKE


    fun sendPacket(packet: Packet) {
        channel.writeAndFlush(packet)
    }

    fun beginPlayState(username: String) {
        val player = Player(
            server,
            this,
            UUID.nameUUIDFromBytes("OfflinePlayer:$username".toByteArray()),
            username
        )

        val event = PlayerLoginEvent(player)
        server.globalEventHandler.callCancellable(event) {
            server.players.add(player)

            sendPacket(
                PacketOutLoginSuccess(
                    player.uuid,
                    player.username
                )
            )

            state = PacketState.PLAY
        }
        if (event.isCancelled) {
            player.kick()
        }
    }

    fun disconnect(reason: Component) {
        when (state) {
            PacketState.LOGIN -> sendPacket(PacketOutLoginDisconnect(reason))
        }
        if (channel.isOpen) channel.close().awaitUninterruptibly()
    }
}