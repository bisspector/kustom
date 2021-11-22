package me.bisspector.kustom.event

fun interface EventCallback<E: Event> {
    fun run(event: E)
}