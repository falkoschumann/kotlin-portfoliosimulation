package de.muspellheim.portfoliosimulation.backend

import de.muspellheim.portfoliosimulation.backend.adapters.*
import de.muspellheim.portfoliosimulation.backend.domain.*
import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.domain.*
import de.muspellheim.portfoliosimulation.contract.data.messages.commands.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*

class MessageHandlingImpl(
    private val repo: PortfolioRepository = PortfolioRepositoryImpl(),
    private val ex: StockExchangeProvider = StockExchangeProviderImpl()
) : MessageHandling {
    override fun handle(command: UpdatePortfolioCommand): CommandStatus {
        val portfolio = repo.load()
        val currentPrice = ex.getPrice(portfolio.stockSymbols)
        // Convert currency if needed; left out for simplicity
        portfolio.update(currentPrice)
        repo.store(portfolio)
        return Success()
    }

    override fun handle(query: PortfolioQuery): PortfolioQueryResult {
        val portfolio = repo.load()
        val portfolioReturns = calculateReturns(portfolio)

        fun result(): PortfolioQueryResult {
            fun map(e: Portfolio.Stock): PortfolioQueryResult.StockInfo =
                PortfolioQueryResult.StockInfo(
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

    override fun handle(command: BuyStockCommand): CommandStatus {
        TODO("Not yet implemented")
    }

    override fun handle(query: CandidateStocksQuery): CandidateStocksQueryResult {
        val candidates = ex.findCandidates(query.pattern)

        fun map(candidate: CandidateStockInfo): CandidateStocksQueryResult.CandidateStock =
            CandidateStocksQueryResult.CandidateStock(
                name = candidate.name,
                symbol = candidate.symbol,
                currency = candidate.currency,
                price = candidate.price
            )

        return CandidateStocksQueryResult(
            candidates = candidates.map { map(it) }
        )
    }

    override fun handle(command: SellStockCommand): CommandStatus {
        TODO("Not yet implemented")
    }

    override fun handle(query: PortfolioStockQuery): PortfolioStockQueryResult {
        TODO("Not yet implemented")
    }
}
