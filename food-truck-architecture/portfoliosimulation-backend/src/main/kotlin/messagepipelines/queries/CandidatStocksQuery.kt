package de.muspellheim.portfoliosimulation.backend.messagepipelines.queries

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.*
import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.messagehandling.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.*

class CandidatStocksQueryContextModel : MessageContext

class CandidateStocksQueryContextManager : MessageContextManager {
    override fun load(input: Message): MessageContext {
        return CandidatStocksQueryContextModel()
    }

    override fun update(events: Iterable<Event>) {}
}

class CandidateStocksQueryProcessor(val ex: StockExchangeProvider) : MessageProcessor {
    override fun process(input: Message, model: MessageContext): Output {
        val query = input as CandidateStocksQuery

        val candidates = ex.findCandidates(query.pattern)

        return QueryOutput(CandidateStocksQueryResult(candidates = candidates.map {
            CandidateStocksQueryResult.CandidateStock(
                name = it.name,
                symbol = it.symbol,
                currency = it.currency,
                price = it.price,
            )
        }))
    }
}
