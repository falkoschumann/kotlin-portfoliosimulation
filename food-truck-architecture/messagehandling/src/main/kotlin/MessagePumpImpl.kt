package de.muspellheim.portfoliosimulation.messagehandling

import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.messagecontext.*

class MessagePumpImpl(private val es: EventStore) : MessagePump {
    private val messagePipelineDirectory = mutableMapOf<Class<Message>, Pipeline>()

    override fun register(ctxManager: MessageContextManager, processor: MessageProcessor) {
        register(ctxManager::load, processor::process, ctxManager::update)
    }

    override fun register(
        load: Func<Message, MessageContext>,
        process: Func2<Message, MessageContext, Output>,
        update: Action<List<Event>>
    ) {
        // TODO Determine Message type?
        messagePipelineDirectory[Message::class.java] =
            Pipeline(loadContext = load, process = process, updateContext = update)
    }

    override fun handle(inputMessage: Message): Message {
        val pipeline = messagePipelineDirectory[inputMessage.javaClass]
        return handle(pipeline!!, inputMessage)
    }

    private fun handle(pipeline: Pipeline, inputMessage: Message): Message {
        val context = pipeline.loadContext(inputMessage)
        val output = pipeline.process(inputMessage, context)
        return dispatch(output)
    }

    private fun dispatch(output: Output): Message {
        return when (output) {
            is CommandOutput -> {
                es.record((output.events))
                updateContext(output.events)
                output.status
            }
            is QueryOutput -> output.result
            is NotificationOutput -> {
                output.commands.forEach { handle(it) }
                NoResponse
            }
        }
    }

    private fun updateContext(events: List<Event>) {
        messagePipelineDirectory.map { it.value.updateContext }.forEach { update -> update(events) }
    }
}

private data class Pipeline(
    val loadContext: Func<Message, MessageContext>,
    val process: Func2<Message, MessageContext, Output>,
    val updateContext: Action<List<Event>>
)
