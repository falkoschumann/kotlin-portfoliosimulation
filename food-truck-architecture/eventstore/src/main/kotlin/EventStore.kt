package de.muspellheim.portfoliosimulation.eventstore

interface EventStore {
    val onRecorded: Action<List<Event>>
    fun record(event: Event)
    fun record(events: List<Event>)
    fun replay(): Sequence<Event>
    fun replay(vararg eventTypes: Class<out Event>): Iterable<Event>
}
