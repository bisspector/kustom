package me.bisspector.kustom.status

import kotlinx.serialization.Serializable
import me.bisspector.kustom.serializers.ComponentSerializer
import me.bisspector.kustom.serializers.UUIDSerializer
import net.kyori.adventure.text.Component
import java.util.*

@Serializable
data class StatusResponse (
    val version: ServerVersion,
    val players: PlayerList,
    @Serializable(with = ComponentSerializer::class) val description: Component
)

@Serializable
data class ServerVersion(
    val name: String,
    val protocol: Int
)

@Serializable
data class PlayerList (
    val max: Int,
    val online: Int,
    val sample: Set<PlayerInfo> = emptySet()
)

@Serializable
data class PlayerInfo(
    val name: String,
    @Serializable(with = UUIDSerializer::class) val id: UUID
)
