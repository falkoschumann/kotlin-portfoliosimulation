package de.muspellheim.portfoliosimulation.eventstore

class EventStoreImpl(path: String) : EventStore {
    override val onRecorded: Action<List<Event>>
        get() = TODO("Not yet implemented")

    override fun record(event: Event) {
        TODO("Not yet implemented")
    }

    override fun record(events: List<Event>) {
        TODO("Not yet implemented")
    }

    override fun replay(): Iterable<Event> {
        TODO("Not yet implemented")
    }

    override fun replay(vararg eventTypes: Class<out Event>): Iterable<Event> {
        TODO("Not yet implemented")
    }
}
