package de.muspellheim.portfoliosimulation.contract.data.messages.queries

data class PortfolioStockQuery(val pattern: String)

data class PortfolioStockQueryResult(val matchingStocks: List<Pair<String, String>>)
