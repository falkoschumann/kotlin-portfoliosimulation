package de.muspellheim.portfoliosimulation.backend.messagehandlers

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.candidatestocks.*

class CandidateStocksQueryHandler(private val ex: StockExchangeProvider) : CandidateStocksQueryHandling {
    override fun handle(request: CandidateStocksQuery): CandidateStocksQueryResult {
        val candidates = ex.findCandidates(request.pattern)

        fun map(candidate: CandidateStockInfo) = CandidateStocksQueryResult.CandidateStock(
            name = candidate.name,
            symbol = candidate.symbol,
            currency = candidate.currency,
            price = candidate.price
        )

        return CandidateStocksQueryResult(
            candidates = candidates.map { map(it) }
        )
    }
}
