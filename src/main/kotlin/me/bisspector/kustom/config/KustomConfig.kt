package me.bisspector.kustom.config

import net.kyori.adventure.text.TextComponent
import java.net.InetAddress

data class KustomConfig(
    val ip: InetAddress,
    val port: Int,
    val status: StatusConfig
)

data class StatusConfig (
    val motd: TextComponent,
    val maxPlayers: Int
)
