package de.muspellheim.portfoliosimulation.backend.messagepipelines.commands

import de.muspellheim.portfoliosimulation.backend.events.*
import de.muspellheim.portfoliosimulation.contract.messages.commands.*
import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.messagehandling.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.*

class SellStockCommandContextModel(values: Set<String>) : PortfoliosymbolsContextModel(values)

class SellStockCommandContextManager(es: EventStore) :
    PortfoliosymbolsContextManager(es, SellStockCommandContextModel::class.java)

class SellStockCommandProcessor : MessageProcessor {
    override fun process(input: Message, model: MessageContext): Output {
        val cmd = input as SellStockCommand
        val cmdModel = model as SellStockCommandContextModel

        if (cmdModel.values.contains(cmd.stockSymbol)) {
            return CommandOutput(
                Success,
                listOf(
                    StockSold(symbol = cmd.stockSymbol)
                )
            )
        }
        return CommandOutput(Failure("Stock '${cmd.stockSymbol}' not in portfolio. It cannot be sold!"))
    }
}
