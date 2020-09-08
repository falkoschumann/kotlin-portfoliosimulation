package de.muspellheim.portfoliosimulation.messagehandling.pipeline

import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.messagehandling.*

interface Output

data class CommandOutput(val status: CommandStatus, val events: List<Event>) : Output

fun SuccessOutput(status: Success, events: List<Event>) = CommandOutput(status, events)

fun FailureOutput(failure: Failure) = CommandOutput(failure, emptyList())

data class NotificationOutput(val commands: List<Command>) : Output

data class QueryOutput(val result: QueryResult) : Output
