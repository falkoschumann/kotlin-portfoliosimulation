package de.muspellheim.portfoliosimulation.contract.data.messages.queries

class PortfolioQuery()

data class PortfolioQueryResult(
    val stocks: List<StockInfo>,
    val portfolioValue: Double,
    val portfolioRateOfReturn: Double
) {
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
