package de.muspellheim.portfoliosimulation.eventstore

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.nio.charset.*
import java.nio.file.*
import kotlin.streams.*

class EventStoreImpl(private val path: Path) : EventStore {
    override val onRecorded = Action<List<Event>>()

    init {
        if (Files.exists(path).not()) {
            Files.createDirectories(path)
        }
    }

    override fun record(event: Event) {
        record(listOf(event))
    }

    override fun record(events: List<Event>) {
        val index = Files.list(path).count()
        events.forEach { store(it, index + 1) }
        onRecorded(events)
    }

    override fun replay(): Iterable<Event> {
        return Files.find(path, 1, { p, _ -> p.toString().endsWith(".txt") })
            .sorted { p1, p2 -> p1.toString().compareTo(p2.toString()) }
            .map { load(it) }
            .toList()
    }

    override fun replay(vararg eventTypes: Class<out Event>): Iterable<Event> {
        return replay().filter { eventTypes.contains(it.javaClass) }
    }

    private fun store(event: Event, index: Long) {
        val text = Json.encodeToString(event)
        write(text, index)
    }

    private fun write(text:String, index:Long) {
        val filename = String.format("%d8", index)
        val filepath = path.resolve("${filename}.txt")
        Files.writeString(filepath, text, StandardCharsets.UTF_8)
    }

    private fun load(file: Path): Event {
        val text = Files.readString(file)
        return Json.decodeFromString(text)
    }

    override fun close() {}
}
