package me.bisspector.kustom.event

import kotlin.reflect.KClass

interface EventHandler {

    val eventCallbacks: HashMap<KClass<out Event>, MutableCollection<EventCallback<*>>>

    fun <E: Event> addEventCallback(eventKClass: KClass<E>, callback: EventCallback<E>): Boolean =
        getEventCallbacks(eventKClass).add(callback)

    fun <E: Event> removeEventCallback(eventKClass: KClass<E>, callback: EventCallback<E>): Boolean =
        getEventCallbacks(eventKClass).remove(callback)

    fun <E: Event> getEventCallbacks(eventKClass: KClass<E>): MutableCollection<EventCallback<*>> =
        eventCallbacks.computeIfAbsent(eventKClass) { mutableSetOf() }

    fun <E: Event> call(event: E) = runEvent(event)

    fun <E> callCancellable(event: E, successCallback: Runnable) where E: Event, E: CancellableEvent {
        call(event)
        if (!event.isCancelled) {
            successCallback.run()
        }
    }

    // TODO: do something about this unchecked cast
    @Suppress("UNCHECKED_CAST")
    private fun <E: Event> runEvent(event: E) = getEventCallbacks(event::class).forEach {
        (it as EventCallback<E>).run(event)
    }
}