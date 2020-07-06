package de.muspellheim.portfoliosimulation.contract.messages.queries.candidatestocks

import de.muspellheim.portfoliosimulation.contract.messages.Query
import de.muspellheim.portfoliosimulation.contract.messages.QueryResult

data class CandidateStocksQuery(val pattern: String) : Query

data class CandidateStocksQueryResult(val candidates: List<CandidateStock>) : QueryResult {
    data class CandidateStock(
            val name: String,
            val symbol: String,
            val currency: String,
            val price: Double
    )
}
