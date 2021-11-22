package me.bisspector.kustom.instance

import java.util.concurrent.CopyOnWriteArraySet

class InstanceManager {
    val instances = CopyOnWriteArraySet<Instance>()

    fun tick() {
        instances.forEach {
            it.tick()
        }
    }
}
