package de.muspellheim.portfoliosimulation.backend.messagehandlers

import de.muspellheim.portfoliosimulation.backend.domain.*
import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.domain.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.portfolio.*

class PortfolioQueryHandler(private val repo: PortfolioRepository) : PortfolioQueryHandling {
    override fun handle(request: PortfolioQuery): PortfolioQueryResult {
        val portfolio = repo.load()
        val portfolioReturns = calculateReturns(portfolio)

        fun result(): PortfolioQueryResult {
            fun map(e: Portfolio.Stock) = PortfolioQueryResult.StockInfo(
                name = e.name,
                symbol = e.symbol,
                currency = e.currency,
                qty = e.qty,
                buyingPrice = e.buyingPrice,
                currentPrice = e.currentPrice,

                buyingValue = e.qty * e.buyingPrice,
                currentValue = e.qty * e.currentPrice,

                returnValue = portfolioReturns.returns.getValue(e.symbol).returnValue,
                rateOfReturn = portfolioReturns.returns.getValue(e.symbol).rateOfReturn
            )

            return PortfolioQueryResult(
                portfolioValue = portfolio.entries.map { it.qty * it.currentPrice }.sum(),
                portfolioRateOfReturn = portfolioReturns.totalRateOfReturn,
                stocks = portfolio.entries.map { map(it) }
            )
        }

        return result()
    }
}
