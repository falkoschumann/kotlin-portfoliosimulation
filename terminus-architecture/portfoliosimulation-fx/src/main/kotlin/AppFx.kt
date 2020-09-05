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

        pqh = PortfolioQueryHandler(repo)
        upc = UpdatePortfolioCommandHandler(repo, ex)
        csq = CandidateStocksQueryHandler(ex)
        bsc = BuyStockCommandHandler(repo)
        psq = PortfolioStockQueryHandler(repo)
        ssc = SellStockCommandHandler(repo)
    }

    override fun start(primaryStage: Stage) {
        // Build
        val portfolioDialog = PortfolioDialog(primaryStage)

        // Bind
        portfolioDialog.onPortfolioQuery = {
            val result = pqh.handle(it)
            portfolioDialog.display(result)
        }
        portfolioDialog.onUpdatePortfolioCommand = {
            upc.handle(it)
            val result = pqh.handle(PortfolioQuery())
            portfolioDialog.display(result)
        }
        portfolioDialog.onSellStockCommand = {
            ssc.handle(it)
        }
        portfolioDialog.onCandidateStocksQuery = {
            val result = csq.handle(it)
            portfolioDialog.display(result)
        }
        portfolioDialog.onBuyStockCommand = {
            bsc.handle(it)
        }

        // Run
        portfolioDialog.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(AppFx::class.java, *args)
}
