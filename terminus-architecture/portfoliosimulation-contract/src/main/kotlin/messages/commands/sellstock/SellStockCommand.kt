package de.muspellheim.portfoliosimulation.contract.messages.commands.sellstock

import de.muspellheim.portfoliosimulation.contract.messages.*

data class SellStockCommand(val stockSymbol: String) : Command
