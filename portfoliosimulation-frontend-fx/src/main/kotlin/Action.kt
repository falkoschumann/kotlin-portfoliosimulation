package de.muspellheim.portfoliosimulation.frontend.fx

import java.util.concurrent.*

class Action<T> {
    private val handlers = CopyOnWriteArrayList<ActionHandler<T>>()

    operator fun invoke(message: T) {
        handlers.forEach { it(message) }
    }

    operator fun plusAssign(handler: ActionHandler<T>) {
        handlers += handler;
    }

    operator fun minusAssign(handler: ActionHandler<T>) {
        handlers -= handler;
    }
}

typealias ActionHandler<T> = (action: T) -> Unit
