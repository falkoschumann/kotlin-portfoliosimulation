package de.muspellheim.portfoliosimulation

import de.muspellheim.portfoliosimulation.backend.adapters.*
import de.muspellheim.portfoliosimulation.backend.messagehandlers.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.portfolio.*
import de.muspellheim.portfoliosimulation.frontend.*

fun main() {
    val repo = PortfolioRepositoryImpl()
    val ex = StockExchangeProviderImpl()

    val pqh = PortfolioQueryHandler(repo)
    val upc = UpdatePortfolioCommandHandler(repo, ex)
    val csq = CandidateStocksQueryHandler(ex)
    val bsc = BuyStockCommandHandler(repo)
    val psq = PortfolioStockQueryHandler(repo)
    val ssc = SellStockCommandHandler(repo)

    val frontend = UserInterface()

    frontend.onPortfolioQuery += {
        val result = pqh.handle(it)
        frontend.display(result)
    }

    frontend.onUpdatePortfolioCommand += {
        upc.handle(it)
        val result = pqh.handle(PortfolioQuery())
        frontend.display(result)
    }

    frontend.onCandidateStocksQuery += {
        val result = csq.handle(it)
        val cmd = frontend.selectStockToBuy(result)
        if (cmd != null) {
            bsc.handle(cmd)
            frontend.displayBuyConfirmation(cmd.stockSymbol, cmd.qty, cmd.stockPrice)
        }
    }

    frontend.onPortfolioStockQuery += {
        val result = psq.handle(it)
        val cmd = frontend.selectStockToSell(result)
        if (cmd != null) {
            ssc.handle(cmd)
            frontend.displaySellConfirmation(cmd.stockSymbol)
        }
    }

    frontend.run()
}
