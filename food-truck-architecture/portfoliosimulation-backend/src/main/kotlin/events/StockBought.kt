package de.muspellheim.portfoliosimulation.backend.events

import de.muspellheim.portfoliosimulation.eventstore.*
import java.time.*

data class StockBought(
    val name: String,
    val symbol: String,
    val currency: String,
    val qty: Int,
    val price: Double,
    override val timestamp: Instant
) : Event()
