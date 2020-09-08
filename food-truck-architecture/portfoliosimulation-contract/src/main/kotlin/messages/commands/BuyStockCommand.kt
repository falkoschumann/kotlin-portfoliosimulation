package de.muspellheim.portfoliosimulation.contract.messages.commands

import java.time.*
import de.muspellheim.portfoliosimulation.messagehandling.*

data class BuyStockCommand(
        val stockName: String,
        val stockSymbol: String,
        val stockPriceCurrency: String,
        val qty: Int,
        val stockPrice: Double,
        val bought: LocalDate
) : Command
