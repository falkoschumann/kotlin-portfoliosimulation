package de.muspellheim.portfoliosimulation.fx

import de.muspellheim.portfoliosimulation.backend.adapters.*
import de.muspellheim.portfoliosimulation.backend.events.*
import de.muspellheim.portfoliosimulation.backend.messagepipelines.commands.*
import de.muspellheim.portfoliosimulation.backend.messagepipelines.queries.*
import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.messages.commands.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.*
import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.frontend.fx.*
import de.muspellheim.portfoliosimulation.messagehandling.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.*
import javafx.application.*
import javafx.stage.*
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.*
import java.nio.file.*

class AppFx : Application() {
    private lateinit var ex: StockExchangeProvider
    private lateinit var json: Json
    private lateinit var es: EventStore
    private lateinit var msgpump: MessagePump

    override fun init() {
        ex = StockExchangeProviderImpl()

        val module = SerializersModule {
            polymorphic(Event::class) {
                subclass(StockBought::class)
                subclass(StockPriceUpdated::class)
                subclass(StockSold::class)
            }
        }
        json = Json {
            serializersModule = module
            prettyPrint = true
        }
        es = EventStoreImpl(Paths.get("eventstream.db"), json)
        msgpump = MessagePumpImpl(es)
        var mcm: MessageContextManager
        var mp: MessageProcessor

        mcm = BuyStockCommandContextManager()
        mp = BuyStockCommandProcessor()
        msgpump.register(BuyStockCommand::class.java, mcm, mp)

        mcm = SellStockCommandContextManager(es)
        mp = SellStockCommandProcessor()
        msgpump.register(SellStockCommand::class.java, mcm, mp)

        mcm = UpdatePortfolioCommandContextManager(es)
        mp = UpdatePortfolioCommandProcessor(ex)
        msgpump.register(UpdatePortfolioCommand::class.java, mcm, mp)

        mcm = CandidateStocksQueryContextManager()
        mp = CandidateStocksQueryProcessor(ex)
        msgpump.register(CandidateStocksQuery::class.java, mcm, mp)

        mcm = PortfolioQueryContextManager(es)
        mp = PortfolioQueryProcessor()
        msgpump.register(PortfolioQuery::class.java, mcm, mp)

        mcm = PortfolioStockQueryContextManager(es)
        mp = PortfolioStockQueryProcessor()
        msgpump.register(PortfolioStockQuery::class.java, mcm, mp)
    }

    override fun start(primaryStage: Stage) {
        // Build
        val portfolioDialog = PortfolioDialog(primaryStage)

        // Bind
        portfolioDialog.onPortfolioQuery = {
            val result = msgpump.handle(it) as PortfolioQueryResult
            portfolioDialog.display(result)
        }
        portfolioDialog.onUpdatePortfolioCommand = {
            msgpump.handle(it)
            val result = msgpump.handle(PortfolioQuery()) as PortfolioQueryResult
            portfolioDialog.display(result)
        }
        portfolioDialog.onSellStockCommand = {
            msgpump.handle(it)
        }
        portfolioDialog.onCandidateStocksQuery = {
            val result = msgpump.handle(it) as CandidateStocksQueryResult
            portfolioDialog.display(result)
        }
        portfolioDialog.onBuyStockCommand = {
            msgpump.handle(it)
        }

        // Run
        portfolioDialog.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(AppFx::class.java, *args)
}
