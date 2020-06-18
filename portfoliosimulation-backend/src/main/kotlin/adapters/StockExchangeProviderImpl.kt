package de.muspellheim.portfoliosimulation.backend.adapters

import de.muspellheim.portfoliosimulation.backend.adapters.alphavantage.*
import de.muspellheim.portfoliosimulation.contract.*

class StockExchangeProviderImpl(private val av: AlphaVantageProvider = AlphaVantageProviderImpl()) :
    StockExchangeProvider {
    override fun getPrice(symbols: List<String>): List<Pair<String, Double>> {
        val prices = mutableListOf<Pair<String, Double>>()
        for (symbol in symbols) {
            try {
                val quote = av.getQuote(symbol)
                prices.add(Pair(symbol, quote.price))
            } catch (ex: Exception) {
                prices.add(Pair(symbol, 0.0))
            }
        }
        return prices.toList()
    }

    override fun findCandidates(pattern: String): List<CandidateStockInfo> {
        TODO("Not yet implemented")
    }
}