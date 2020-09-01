package de.muspellheim.portfoliosimulation.fx

import de.muspellheim.portfoliosimulation.backend.adapters.PortfolioRepositoryImpl
import de.muspellheim.portfoliosimulation.backend.adapters.StockExchangeProviderImpl
import de.muspellheim.portfoliosimulation.backend.messagehandlers.BuyStockCommandHandler
import de.muspellheim.portfoliosimulation.backend.messagehandlers.CandidateStocksQueryHandler
import de.muspellheim.portfoliosimulation.backend.messagehandlers.PortfolioQueryHandler
import de.muspellheim.portfoliosimulation.backend.messagehandlers.PortfolioStockQueryHandler
import de.muspellheim.portfoliosimulation.backend.messagehandlers.SellStockCommandHandler
import de.muspellheim.portfoliosimulation.backend.messagehandlers.UpdatePortfolioCommandHandler
import de.muspellheim.portfoliosimulation.contract.messages.commands.buystock.BuyStockCommandHandling
import de.muspellheim.portfoliosimulation.contract.messages.commands.sellstock.SellStockCommandHandling
import de.muspellheim.portfoliosimulation.contract.messages.commands.updateportfolio.UpdatePortfolioCommandHandling
import de.muspellheim.portfoliosimulation.contract.messages.queries.candidatestocks.CandidateStocksQueryHandling
import de.muspellheim.portfoliosimulation.contract.messages.queries.portfolio.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.portfoliostock.PortfolioStockQueryHandling
import de.muspellheim.portfoliosimulation.frontend.fx.*
import javafx.application.Application
import javafx.scene.*
import javafx.stage.Stage

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

    override fun start(stage: Stage) {
        val portfolioViewController = createPortfolioViewController()
        portfolioViewController.onPortfolioQuery += {
            val result = pqh.handle(it)
            portfolioViewController.display(result)
        }
        portfolioViewController.onUpdatePortfolioCommand += {
            upc.handle(it)
            val result = pqh.handle(PortfolioQuery())
            portfolioViewController.display(result);
        }

        // TODO: Bind

        stage.title = "Portfolio Simulation"
        stage.scene = Scene(portfolioViewController.view, 1280.0, 680.0)
        stage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(AppFx::class.java, *args)
}
