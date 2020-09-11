package de.muspellheim.portfoliosimulation.messagehandling.pipeline.messagecontext

import de.muspellheim.portfoliosimulation.eventstore.*

interface MessageContextBuilder {
    fun update(events: Iterable<Event>)
}
