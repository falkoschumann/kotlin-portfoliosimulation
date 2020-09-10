package de.muspellheim.portfoliosimulation.messagehandling

import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.messagecontext.*

typealias Func<I, O> = (arg: I) -> O
typealias Func2<I1, I2, O> = (arg1: I1, arg2: I2) -> O
typealias Action<I> = (arg: I) -> Unit

interface MessagePump {
    fun register(ctxManager: MessageContextManager, processor: MessageProcessor)
    fun register(
        load: Func<Message, MessageContext>,
        process: Func2<Message, MessageContext, Output>,
        update: Action<List<Event>>
    )

    fun handle(inputMessage: Message)
}
