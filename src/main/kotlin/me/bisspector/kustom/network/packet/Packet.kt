package me.bisspector.kustom.network.packet

import io.netty.buffer.ByteBuf
import me.bisspector.kustom.network.packet.state.PacketState
import kotlin.UnsupportedOperationException

interface Packet {
    val info: PacketInfo

    fun read(buf: ByteBuf) {
        throw UnsupportedOperationException("Read is not supported in $javaClass")
    }
    fun write(buf: ByteBuf) {
        throw UnsupportedOperationException("Write is not supported in $javaClass")
    }
}

data class PacketInfo(val id: Int, val state: PacketState)