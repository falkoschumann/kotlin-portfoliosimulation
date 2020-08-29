package de.muspellheim.portfoliosimulation.fx

import de.muspellheim.portfoliosimulation.backend.adapters.*
import de.muspellheim.portfoliosimulation.backend.messagehandlers.*
import de.muspellheim.portfoliosimulation.contract.messages.commands.buystock.*
import de.muspellheim.portfoliosimulation.contract.messages.commands.sellstock.*
import de.muspellheim.portfoliosimulation.contract.messages.commands.updateportfolio.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.candidatestocks.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.portfolio.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.portfoliostock.*
import de.muspellheim.portfoliosimulation.frontend.fx.*
import javafx.application.*
import javafx.stage.*

class AppFx : Application() {
    private lateinit var pqh: PortfolioQueryHandling
    private lateinit var upc: UpdatePortfolioCommandHandling
    private lateinit var csq: CandidateStocksQueryHandling
    private lateinit var bsc: BuyStockCommandHandling
    private lateinit var psq: PortfolioStockQueryHandling
    private lateinit var ssc: SellStockCommandHandling

    override fun init() {
        val repo = PortfolioRepositoryImpl()
        val ex = StockExchangeProviderImpl()

        val pqh = PortfolioQueryHandler(repo)
        val upc = UpdatePortfolioCommandHandler(repo, ex)
        val csq = CandidateStocksQueryHandler(ex)
        val bsc = BuyStockCommandHandler(repo)
        val psq = PortfolioStockQueryHandler(repo)
        val ssc = SellStockCommandHandler(repo)
    }

    override fun start(stage: Stage) {
        val frontend = createPortfolioDialog(stage)

        // TODO: Bind

        frontend.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(AppFx::class.java, *args)
}
