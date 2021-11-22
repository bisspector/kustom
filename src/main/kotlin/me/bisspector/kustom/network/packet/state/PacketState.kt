package me.bisspector.kustom.network.packet.state

import me.bisspector.kustom.network.packet.Packet
import me.bisspector.kustom.network.packet.`in`.handshake.PacketInHandshake
import me.bisspector.kustom.network.packet.`in`.login.PacketInLoginStart
import me.bisspector.kustom.network.packet.`in`.status.PacketInPing
import me.bisspector.kustom.network.packet.`in`.status.PacketInStatusRequest

enum class PacketState(
    val id: Int,
    private val packets: Map<Int, () -> Packet>,
) {
    HANDSHAKE(
        0,
        mapOf(
            0x0 to ::PacketInHandshake
        ),
    ),
    STATUS(
        1,
        mapOf(
            0x0 to ::PacketInStatusRequest,
            0x01 to ::PacketInPing
        ),
    ),
    LOGIN(
        2,
        mapOf(
            0x0 to ::PacketInLoginStart,
        ),
    ),
    PLAY(
        3,
        emptyMap(),
    );

    fun createPacket(id: Int): Packet? {
        return packets[id]?.invoke()
    }

    companion object {
        fun fromId(id: Int): PacketState {
            return values().first { it.id == id }
        }
    }
}