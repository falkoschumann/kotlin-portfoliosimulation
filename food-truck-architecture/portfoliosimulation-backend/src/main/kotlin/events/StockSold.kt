package de.muspellheim.portfoliosimulation.backend.events

import de.muspellheim.portfoliosimulation.eventstore.*

data class StockSold(val symbol: String) : Event()
