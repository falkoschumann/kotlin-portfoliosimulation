package de.muspellheim.portfoliosimulation.backend.events

import de.muspellheim.portfoliosimulation.eventstore.*
import kotlinx.serialization.*
import java.time.*

@Serializable
data class StockPriceUpdated(
    val symbol: String,
    val price: Double,
    @Serializable(with = InstantAsLongSerializer::class)
    override val timestamp: Instant = Instant.now()
) : Event()
