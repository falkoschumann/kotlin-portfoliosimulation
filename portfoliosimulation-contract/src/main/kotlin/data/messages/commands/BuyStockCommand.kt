package de.muspellheim.portfoliosimulation.contract.data.messages.commands

import java.time.LocalDateTime

data class BuyStockCommand(
    val stockName: String,
    val stockSymbol: String,
    val stockPriceCurrency: String,
    val qty: Int,
    val stockPrice: Double,
    val bought: LocalDateTime
)
