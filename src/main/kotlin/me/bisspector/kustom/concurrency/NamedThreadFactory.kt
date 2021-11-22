package me.bisspector.kustom.concurrency

import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class NamedThreadFactory(private val nameFormat: String): ThreadFactory {
    private val threadNumber = AtomicInteger(1)
    private val factory = Executors.defaultThreadFactory()

    override fun newThread(task: Runnable): Thread = factory.newThread(task).apply {
        name = nameFormat.format(threadNumber.getAndIncrement())
    }
}