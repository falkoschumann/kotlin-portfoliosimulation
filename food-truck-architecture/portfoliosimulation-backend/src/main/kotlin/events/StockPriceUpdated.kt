package de.muspellheim.portfoliosimulation.backend.events

import de.muspellheim.portfoliosimulation.eventstore.*

data class StockPriceUpdated(val symbol: String,
                             val price: Double
) : Event()
