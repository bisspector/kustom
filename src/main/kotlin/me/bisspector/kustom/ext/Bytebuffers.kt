package me.bisspector.kustom.ext

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.EncoderException
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import java.io.IOException
import java.net.InetAddress
import java.util.*
import kotlin.experimental.and

fun ByteBuf.readVarInt(): Int {
    var numRead = 0
    var result = 0
    var read: Byte
    do {
        read = readByte()
        val value = (read and 127).toInt()
        result = result or (value shl 7 * numRead)
        numRead++
        if (numRead > 5) throw RuntimeException("VarInt is too big")
    } while (read and 128.toByte() != 0.toByte())

    return result
}

fun ByteBuf.writeVarInt(varInt: Int) {
    var i = varInt
    while (i and -128 != 0) {
        writeByte(i and 127 or 128)
        i = i ushr 7
    }

    writeByte(i)
}

fun ByteBuf.readString(maxLength: Short = Short.MAX_VALUE): String {
    val length = readVarInt()

    return when {
        length > maxLength * 4 ->
            throw IOException("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")")
        length < 0 ->
            throw IOException("The received encoded string buffer length is less than zero! Weird string!")
        else -> {
            val array = ByteArray(length)
            this.readBytes(array)
            val s = array.decodeToString()
            if (s.length > maxLength) {
                throw IOException("The received string length is longer than maximum allowed ($length > $maxLength)")
            } else {
                s
            }
        }
    }
}


fun ByteBuf.writeString(string: String, maxLength: Short = Short.MAX_VALUE) {
    val bytes = string.toByteArray(Charsets.UTF_8)
    if (bytes.size > maxLength) {
        throw EncoderException("String too big (was " + bytes.size + " bytes encoded, max " + maxLength + ")")
    } else {
        writeVarInt(bytes.size)
        writeBytes(bytes)
    }
}

fun ByteBuf.writeUUID(uuid: UUID) {
    writeLong(uuid.mostSignificantBits)
    writeLong(uuid.leastSignificantBits)
}

fun ByteBuf.writeChat(reason: Component) {
    writeString(GsonComponentSerializer.gson().serialize(reason))
}

fun ByteBuf.readAvailableBytes(length: Int): ByteArray {
    if (hasArray()) return readBytes(length).array()

    val bytes = ByteArray(length)
    readBytes(bytes, readerIndex(), length)
    return bytes
}

fun String.toInetAddress(): InetAddress {
    return InetAddress.getByName(this)
}

fun Int.varIntSize(): Int {
    for (i in 1 until 5) {
        if ((this and (-1 shl i * 7)) != 0) continue
        return i
    }
    return 5
}