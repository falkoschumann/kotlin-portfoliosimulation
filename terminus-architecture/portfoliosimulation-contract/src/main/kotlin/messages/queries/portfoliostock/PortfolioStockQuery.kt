package de.muspellheim.portfoliosimulation.contract.messages.queries.portfoliostock

import de.muspellheim.portfoliosimulation.contract.messages.*

data class PortfolioStockQuery(val pattern: String) : Query

data class PortfolioStockQueryResult(val matchingStocks: List<Pair<String, String>>) : QueryResult
