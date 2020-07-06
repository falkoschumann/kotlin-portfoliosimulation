package de.muspellheim.portfoliosimulation.contract.messages.commands.buystock

import de.muspellheim.portfoliosimulation.contract.messages.Command
import java.time.LocalDate

data class BuyStockCommand(
        val stockName: String,
        val stockSymbol: String,
        val stockPriceCurrency: String,
        val qty: Int,
        val stockPrice: Double,
        val bought: LocalDate
) : Command
