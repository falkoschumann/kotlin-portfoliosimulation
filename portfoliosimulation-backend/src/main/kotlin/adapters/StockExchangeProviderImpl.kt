package de.muspellheim.portfoliosimulation.backend.adapters

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.domain.*

class StockExchangeProviderImpl : StockExchangeProvider {
    override fun getPrice(symbols: List<String>): List<Pair<String, Double>> {
        TODO("Not yet implemented")
    }

    override fun findCandidates(pattern: String): List<CandidateStockInfo> {
        TODO("Not yet implemented")
    }
}