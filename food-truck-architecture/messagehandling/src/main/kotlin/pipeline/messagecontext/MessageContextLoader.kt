package de.muspellheim.portfoliosimulation.messagehandling.pipeline.messagecontext

import de.muspellheim.portfoliosimulation.messagehandling.*

interface MessageContextLoader {
    fun load(input: Message): MessageContext
}
