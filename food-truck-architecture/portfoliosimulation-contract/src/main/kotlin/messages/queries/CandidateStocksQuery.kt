package de.muspellheim.portfoliosimulation.contract.messages.queries

data class CandidateStocksQuery(val pattern: String)

data class CandidateStocksQueryResult(val candidates: List<CandidateStock>) {
    data class CandidateStock(
        val name: String,
        val symbol: String,
        val currency: String,
        val price: Double
    )
}
