package de.muspellheim.portfoliosimulation.contract.messages.commands

import de.muspellheim.portfoliosimulation.messagehandling.*

data class SellStockCommand(val stockSymbol: String) : Command
