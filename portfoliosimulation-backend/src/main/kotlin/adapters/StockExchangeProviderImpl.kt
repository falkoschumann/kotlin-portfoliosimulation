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
                System.err.println(ex)
                prices.add(Pair(symbol, 0.0))
            }
        }
        return prices.toList()
    }

    override fun findCandidates(pattern: String): List<CandidateStockInfo> {
        val stockInfos = av.findMatchingStocks(pattern)

        fun getQuote(symbol: String): Double =
            try {
                av.getQuote(symbol).price
            } catch (ex: Exception) {
                System.err.println(ex)
                0.0;
            }

        fun compilePrices(symbols: List<String>): List<Double> = symbols.map { getQuote(it) }

        val stockPrices = compilePrices(stockInfos.map { it.symbol })
        val candidates = stockInfos.zip(stockPrices) { si: StockInfo, p: Double ->
            CandidateStockInfo(
                name = si.name,
                symbol = si.symbol,
                currency = si.currency,
                price = p
            )
        }
        return candidates.filter { it.price > 0 }
    }
}
