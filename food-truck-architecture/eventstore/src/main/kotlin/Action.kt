package de.muspellheim.portfoliosimulation.eventstore

import java.util.concurrent.*

class Action<T> {
    private val handlers = CopyOnWriteArrayList<Handler<T>>()

    operator fun plusAssign(handler: Handler<T>) {
        handlers += handler
    }

    operator fun minusAssign(handler: Handler<T>) {
        handlers -= handler
    }

    operator fun invoke(obj: T) {
        handlers.forEach { it(obj) }
    }
}

typealias Handler<T> = (obj: T) -> Unit
