package de.muspellheim.portfoliosimulation.frontend.util

import java.util.concurrent.*

class Action<T> {
    private val handlers = CopyOnWriteArrayList<ActionHandler<T>>()

    operator fun plusAssign(handler: ActionHandler<T>) {
        handlers += handler
    }

    operator fun minusAssign(handler: ActionHandler<T>) {
        handlers -= handler
    }

    operator fun invoke(obj: T) {
        handlers.forEach { it(obj) }
    }
}

typealias ActionHandler<T> = (obj: T) -> Unit
