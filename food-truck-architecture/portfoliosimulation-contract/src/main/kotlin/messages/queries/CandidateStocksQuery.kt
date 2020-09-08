package de.muspellheim.portfoliosimulation.contract.messages.queries

import de.muspellheim.portfoliosimulation.messagehandling.*

data class CandidateStocksQuery(val pattern: String) : Query

data class CandidateStocksQueryResult(val candidates: List<CandidateStock>) : QueryResult {
    data class CandidateStock(
        val name: String,
        val symbol: String,
        val currency: String,
        val price: Double
    )
}
