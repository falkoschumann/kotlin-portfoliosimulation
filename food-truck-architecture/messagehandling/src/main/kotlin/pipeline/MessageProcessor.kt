package de.muspellheim.portfoliosimulation.messagehandling.pipeline

import de.muspellheim.portfoliosimulation.messagehandling.*

interface MessageProcessor {
    fun process(input: Message, model: MessageContext): Output
}
