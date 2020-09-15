package de.muspellheim.portfoliosimulation.backend.events

import de.muspellheim.portfoliosimulation.eventstore.*
import kotlinx.serialization.*
import java.time.*

@Serializable
data class StockSold(
    val symbol: String,
    @Serializable(with = InstantAsLongSerializer::class)
    override val timestamp: Instant = Instant.now()
) : Event()
