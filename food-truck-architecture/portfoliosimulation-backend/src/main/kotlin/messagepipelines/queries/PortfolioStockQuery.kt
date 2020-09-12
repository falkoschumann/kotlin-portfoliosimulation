package de.muspellheim.portfoliosimulation.backend.messagepipelines.queries

import de.muspellheim.portfoliosimulation.backend.events.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.*
import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.messagehandling.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.*

class PortfolioStockQueryContextModel(val matchingStocks: List<Pair<String, String>>) : MessageContext {
}

class PortfolioStockQueryContextManager(es: EventStore) : MessageContextManager {
    private val portfolio = mutableMapOf<String, String>()

    init {
        update(es.replay(StockBought::class.java, StockSold::class.java))
    }

    override fun load(input: Message): MessageContext {
        val query = input as PortfolioStockQuery

        val matchingStocks = portfolio
            .filter { it.key.contains(query.pattern, true) || it.key.contains(query.pattern, true) }
            .map { Pair(it.key, it.value) }

        return PortfolioStockQueryContextModel(matchingStocks)
    }

    override fun update(events: Iterable<Event>) {
        events.forEach {
            when (it) {
                is StockBought -> portfolio[it.symbol] = it.name
                is StockSold -> portfolio.remove(it.symbol)
            }
        }
    }
}

class PortfolioStockQueryProcessor : MessageProcessor {
    override fun process(input: Message, model: MessageContext): Output {
        val queryModel = model as PortfolioStockQueryContextModel

        return QueryOutput(
            PortfolioStockQueryResult(
                matchingStocks = queryModel.matchingStocks
            )
        )
    }

}
