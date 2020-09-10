package de.muspellheim.portfoliosimulation.messagehandling.pipeline

import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.messagehandling.*

interface Output

data class CommandOutput(val status: CommandStatus, val events: List<Event>) : Output {
    constructor(failure: Failure) : this(failure, emptyList())
}

data class NotificationOutput(val commands: List<Command>) : Output

data class QueryOutput(val result: QueryResult) : Output
