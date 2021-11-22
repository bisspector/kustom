package me.bisspector.kustom

import me.bisspector.kustom.event.PlayerLoginEvent
import net.kyori.adventure.text.Component

fun main() {
    KustomServer().start("127.0.0.1", 25565)
        .globalEventHandler.addEventCallback(PlayerLoginEvent::class) {
            it.isCancelled = true
        }
}