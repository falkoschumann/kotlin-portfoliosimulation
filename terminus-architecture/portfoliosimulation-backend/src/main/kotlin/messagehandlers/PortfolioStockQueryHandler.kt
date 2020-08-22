package de.muspellheim.portfoliosimulation.backend.messagehandlers

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.domain.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.portfoliostock.*

class PortfolioStockQueryHandler(private val repo: PortfolioRepository) : PortfolioStockQueryHandling {
    override fun handle(request: PortfolioStockQuery): PortfolioStockQueryResult {
        val portfolio = repo.load()
        val matching = portfolio.find(request.pattern)

        fun map(match: Portfolio.Stock) = Pair(match.name, match.symbol)

        return PortfolioStockQueryResult(
            matchingStocks = matching.map { map(it) }
        )
    }
}