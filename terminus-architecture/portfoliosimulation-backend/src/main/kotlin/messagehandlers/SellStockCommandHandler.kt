package de.muspellheim.portfoliosimulation.backend.messagehandlers

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.messages.*
import de.muspellheim.portfoliosimulation.contract.messages.commands.sellstock.*

class SellStockCommandHandler(private val repo: PortfolioRepository) : SellStockCommandHandling {
    override fun handle(request: SellStockCommand): CommandStatus {
        val portfolio = repo.load()

        portfolio.remove(request.stockSymbol)

        repo.store(portfolio)
        return Success()
    }
}
