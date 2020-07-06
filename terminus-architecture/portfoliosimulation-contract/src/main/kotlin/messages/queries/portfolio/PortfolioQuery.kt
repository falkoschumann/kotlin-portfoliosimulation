package de.muspellheim.portfoliosimulation.contract.messages.queries.portfolio

import de.muspellheim.portfoliosimulation.contract.messages.Query
import de.muspellheim.portfoliosimulation.contract.messages.QueryResult

class PortfolioQuery() : Query

data class PortfolioQueryResult(
        val stocks: List<StockInfo>,
        val portfolioValue: Double,
        val portfolioRateOfReturn: Double
) : QueryResult {
    data class StockInfo(
            val name: String,
            val symbol: String,
            val currency: String,
            val qty: Int,
            val buyingPrice: Double,
            val buyingValue: Double,
            val currentPrice: Double,
            val currentValue: Double,
            val returnValue: Double,
            val rateOfReturn: Double
    )
}
