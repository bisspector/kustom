package me.bisspector.kustom.event

import me.bisspector.kustom.entity.Player
import net.kyori.adventure.text.Component

class PlayerLoginEvent(
    val player: Player,
): Event(), CancellableEvent {
    override var isCancelled: Boolean = false
}
