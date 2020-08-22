package de.muspellheim.portfoliosimulation.backend.messagehandlers

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.messages.*
import de.muspellheim.portfoliosimulation.contract.messages.commands.updateportfolio.*

class UpdatePortfolioCommandHandler(private val repo: PortfolioRepository, private val ex: StockExchangeProvider) :
    UpdatePortfolioCommandHandling {
    override fun handle(request: UpdatePortfolioCommand): CommandStatus {
        val portfolio = repo.load()
        val currentPrice = ex.getPrice(portfolio.stockSymbols)
        // Convert currency if needed; left out for simplicity
        portfolio.update(currentPrice)
        repo.store(portfolio)
        return Success()
    }
}
