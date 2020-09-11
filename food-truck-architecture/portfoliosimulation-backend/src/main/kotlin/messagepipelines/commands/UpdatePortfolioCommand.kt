package de.muspellheim.portfoliosimulation.backend.messagepipelines.commands

import de.muspellheim.portfoliosimulation.backend.events.*
import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.messagehandling.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.messagecontext.*

class UpdatePortfolioCommandContextModel(values: Set<String>) : PortfoliosymbolsContextModel(values)

class UpdatePortfolioCommandContextManager(es: EventStore) :
    PortfoliosymbolsContextManager(es, UpdatePortfolioCommandContextModel::class.java)

class UpdatePortfolioCommandProcessor(private val ex: StockExchangeProvider) : MessageProcessor {
    override fun process(input: Message, model: MessageContext): Output {
        val cmdModel = model as UpdatePortfolioCommandContextModel

        val currentPrices = ex.getPrice(cmdModel.values.toList())
        val events = currentPrices.map { StockPriceUpdated(symbol = it.first, price = it.second) }

        return CommandOutput(Success, events)
    }
}
