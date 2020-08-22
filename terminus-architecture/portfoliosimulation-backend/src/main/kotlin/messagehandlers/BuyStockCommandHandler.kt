package de.muspellheim.portfoliosimulation.backend.messagehandlers

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.domain.*
import de.muspellheim.portfoliosimulation.contract.messages.*
import de.muspellheim.portfoliosimulation.contract.messages.commands.buystock.*
import java.time.*

class BuyStockCommandHandler(private val repo: PortfolioRepository) : BuyStockCommandHandling {
    override fun handle(request: BuyStockCommand): CommandStatus {
        val portfolio = repo.load()

        val newStock = Portfolio.Stock(
            name = request.stockName,
            symbol = request.stockSymbol,
            currency = request.stockPriceCurrency,
            qty = request.qty,
            buyingPrice = request.stockPrice,
            bought = request.bought,
            currentPrice = request.stockPrice,
            lastUpdated = LocalDate.now()
        )
        portfolio.entries.add(newStock)

        repo.store(portfolio)
        return Success()
    }
}
