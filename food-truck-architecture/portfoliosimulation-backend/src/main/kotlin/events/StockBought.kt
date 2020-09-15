package de.muspellheim.portfoliosimulation.backend.events

import de.muspellheim.portfoliosimulation.eventstore.*
import kotlinx.serialization.*
import java.time.*

@Serializable
class StockBought(
    val name: String,
    val symbol: String,
    val currency: String,
    val qty: Int,
    val price: Double,
    @Serializable(with = InstantAsLongSerializer::class)
    override val timestamp: Instant = Instant.now()
) : Event()
