package de.muspellheim.portfoliosimulation.messagehandling.pipeline

import de.muspellheim.portfoliosimulation.messagehandling.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.messagecontext.*

interface MessageProcessor {
    fun process(input: Message, model: MessageContext): Output
}
