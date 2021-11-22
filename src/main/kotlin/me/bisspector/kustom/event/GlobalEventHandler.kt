package me.bisspector.kustom.event

import kotlin.reflect.KClass

class GlobalEventHandler: EventHandler {
    override val eventCallbacks = HashMap<KClass<out Event>, MutableCollection<EventCallback<*>>>()
}
