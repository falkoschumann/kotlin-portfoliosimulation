package de.muspellheim.portfoliosimulation.messagehandling.pipeline

import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.messagehandling.*

interface MessageContext

interface MessageContextBuilder {
    fun update(events: Iterable<Event>)
}

interface MessageContextLoader {
    fun load(input: Message): MessageContext
}

interface MessageContextManager : MessageContextLoader, MessageContextBuilder
