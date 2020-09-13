package de.muspellheim.portfoliosimulation.eventstore

import java.io.*

interface EventStore : Closeable {
    val onRecorded: Action<List<Event>>
    fun record(event: Event)
    fun record(events: List<Event>)
    fun replay(): Iterable<Event>
    fun replay(vararg eventTypes: Class<out Event>): Iterable<Event>
}
