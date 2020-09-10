package de.muspellheim.portfoliosimulation.eventstore

import java.time.*
import java.util.*

open class Event(
    val id: String = UUID.randomUUID().toString(),
    open val timestamp: Instant = Instant.now()
)
