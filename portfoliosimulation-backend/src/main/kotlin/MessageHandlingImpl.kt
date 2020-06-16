package de.muspellheim.portfoliosimulation.backend

import de.muspellheim.portfoliosimulation.backend.adapters.*
import de.muspellheim.portfoliosimulation.backend.domain.*
import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.domain.*
import de.muspellheim.portfoliosimulation.contract.data.messages.commands.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*

class MessageHandlingImpl(private val repo: PortfolioRepository = PortfolioRepositoryImpl()) : MessageHandling {
    override fun handle(command: UpdatePortfolioCommand): CommandStatus {
        TODO("Not yet implemented")
    }

    override fun handle(query: PortfolioQuery): PortfolioQueryResult {
        val portfolio = repo.load()
        val portfolioReturns = calculateReturns(portfolio)

        fun result(): PortfolioQueryResult {
            fun map(e: Portfolio.Stock): PortfolioQueryResult.StockInfo {
                return PortfolioQueryResult.StockInfo(
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
            }

            return PortfolioQueryResult(
                portfolioValue = portfolio.entries.map { it.qty * it.currentPrice }.sum(),
                portfolioRateOfReturn = portfolioReturns.totalRateOfReturn,
                stocks = portfolio.entries.map { map(it) }
            )
        }

        return result()
    }

    override fun handle(command: BuyStockCommand): CommandStatus {
        TODO("Not yet implemented")
    }

    override fun handle(query: CandidateStocksQuery): CandidateStocksQueryResult {
        TODO("Not yet implemented")
    }

    override fun handle(command: SellStockCommand): CommandStatus {
        TODO("Not yet implemented")
    }

    override fun handle(query: PortfolioStockQuery): PortfolioStockQueryResult {
        TODO("Not yet implemented")
    }
}
