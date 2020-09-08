package de.muspellheim.portfoliosimulation.contract.messages.queries

import de.muspellheim.portfoliosimulation.messagehandling.*

data class PortfolioStockQuery(val pattern: String) : Query

data class PortfolioStockQueryResult(val matchingStocks: List<Pair<String, String>>) : QueryResult
