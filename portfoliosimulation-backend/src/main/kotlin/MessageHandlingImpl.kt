package de.muspellheim.portfoliosimulation.backend

import de.muspellheim.portfoliosimulation.backend.adapters.*
import de.muspellheim.portfoliosimulation.backend.domain.*
import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.domain.*
import de.muspellheim.portfoliosimulation.contract.data.messages.commands.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*
import java.time.*

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

    override fun handle(command: BuyStockCommand): CommandStatus {
        val portfolio = repo.load()

        val newStock = Portfolio.Stock(
            name = command.stockName,
            symbol = command.stockSymbol,
            currency = command.stockPriceCurrency,
            qty = command.qty,
            buyingPrice = command.stockPrice,
            bought = command.bought,
            currentPrice = command.stockPrice,
            lastUpdated = LocalDate.now()
        )
        portfolio.entries.add(newStock)

        repo.store(portfolio)
        return Success()
    }

    override fun handle(query: CandidateStocksQuery): CandidateStocksQueryResult {
        val candidates = ex.findCandidates(query.pattern)

        fun map(candidate: CandidateStockInfo) = CandidateStocksQueryResult.CandidateStock(
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
        val portfolio = repo.load()

        portfolio.remove(command.stockSymbol)

        repo.store(portfolio)
        return Success()
    }

    override fun handle(query: PortfolioStockQuery): PortfolioStockQueryResult {
        val portfolio = repo.load()
        val matching = portfolio.find(query.pattern)

        fun map(match: Portfolio.Stock) = Pair(match.name, match.symbol)

        return PortfolioStockQueryResult(
            matchingStocks = matching.map { map(it) }
        )
    }
}
