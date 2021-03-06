package de.muspellheim.portfoliosimulation.contract

import de.muspellheim.portfoliosimulation.contract.data.domain.*

data class CandidateStockInfo(
    val name: String,
    val symbol: String,
    val currency: String,
    val price: Double
)

interface StockExchangeProvider {
    fun getPrice(symbols: List<String>): List<Pair<String, Double>>
    fun findCandidates(pattern: String): List<CandidateStockInfo>
}