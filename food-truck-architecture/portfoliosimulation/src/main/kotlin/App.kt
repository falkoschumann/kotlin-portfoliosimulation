package de.muspellheim.portfoliosimulation

import de.muspellheim.portfoliosimulation.backend.adapters.*
import de.muspellheim.portfoliosimulation.backend.messagepipelines.commands.*
import de.muspellheim.portfoliosimulation.backend.messagepipelines.queries.*
import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.*
import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.frontend.*
import de.muspellheim.portfoliosimulation.messagehandling.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.*
import java.nio.file.*

fun main() {
    val ex: StockExchangeProvider = StockExchangeProviderImpl()

    val es: EventStore = EventStoreImpl(Paths.get("eventstream.db"))
    val msgpump: MessagePump = MessagePumpImpl(es)
    var mcm: MessageContextManager
    var mp: MessageProcessor

    mcm = BuyStockCommandContextManager()
    mp = BuyStockCommandProcessor()
    msgpump.register(mcm, mp)

    mcm = SellStockCommandContextManager(es)
    mp = SellStockCommandProcessor()
    msgpump.register(mcm, mp)

    mcm = UpdatePortfolioCommandContextManager(es)
    mp = UpdatePortfolioCommandProcessor(ex)
    msgpump.register(mcm, mp)

    mcm = CandidateStocksQueryContextManager()
    mp = CandidateStocksQueryProcessor(ex)
    msgpump.register(mcm, mp)

    mcm = PortfolioQueryContextManager(es)
    mp = PortfolioQueryProcessor()
    msgpump.register(mcm, mp)

    mcm = PortfolioStockQueryContextManager(es)
    mp = PortfolioStockQueryProcessor()
    msgpump.register(mcm, mp)

    val frontend = UserInterface()

    frontend.onPortfolioQuery += {
        val result = msgpump.handle(it) as PortfolioQueryResult
        frontend.display(result)
    }

    frontend.onUpdatePortfolioCommand += {
        msgpump.handle(it)
        val result = msgpump.handle(PortfolioQuery()) as PortfolioQueryResult
        frontend.display(result)
    }

    frontend.onCandidateStocksQuery += {
        val result = msgpump.handle(it) as CandidateStocksQueryResult
        val cmd = frontend.selectStockToBuy(result)
        if (cmd != null) {
            msgpump.handle(cmd)
            frontend.displayBuyConfirmation(cmd.stockSymbol, cmd.qty, cmd.stockPrice)
        }
    }

    frontend.onPortfolioStockQuery += {
        val result = msgpump.handle(it) as PortfolioStockQueryResult
        val cmd = frontend.selectStockToSell(result)
        if (cmd != null) {
            msgpump.handle(cmd)
            frontend.displaySellConfirmation(cmd.stockSymbol)
        }
    }

    frontend.run()
}
