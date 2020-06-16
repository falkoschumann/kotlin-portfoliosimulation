package de.muspellheim.portfoliosimulation.contract.data.messages.queries

data class PortfolioStockQuery(val pattern: String)

data class PortfolioStockQueryResult(val matchingStocks: List<Stock>) {
    data class Stock(val name: String, val symbol: String)
}
