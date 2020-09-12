package de.muspellheim.portfoliosimulation.backend.messagepipelines.queries

import de.muspellheim.portfoliosimulation.backend.domain.*
import de.muspellheim.portfoliosimulation.backend.events.*
import de.muspellheim.portfoliosimulation.contract.data.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.*
import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.messagehandling.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.*
import java.time.*

class PortfolioQueryContextModel(val entries: Iterable<StockInfo>) : MessageContext {
    data class StockInfo(
        val name: String,
        val symbol: String,
        val currency: String,
        var qty: Int,
        var buyingPrice: Double,
        val bought: LocalDate,
        var currentPrice: Double,
        var lastUpdated: LocalDate,
    )
}

class PortfolioQueryContextManager(private val es: EventStore) : MessageContextManager {
    private val portfolio = mutableMapOf<String, PortfolioQueryContextModel.StockInfo>()

    init {
        update(es.replay(StockBought::class.java, StockSold::class.java, StockPriceUpdated::class.java))
    }

    override fun load(input: Message): MessageContext {
        return PortfolioQueryContextModel(entries = portfolio.values)
    }

    override fun update(events: Iterable<Event>) {
        events.forEach {
            when (it) {
                is StockBought -> {
                    val stockinfo = portfolio[it.symbol]
                    if (stockinfo != null) {
                        stockinfo.qty += it.qty
                        stockinfo.buyingPrice = (stockinfo.buyingPrice + it.price) / 2.0
                    } else {
                        portfolio[it.symbol] = PortfolioQueryContextModel.StockInfo(
                            name = it.name,
                            symbol = it.symbol,
                            currency = it.currency,
                            qty = it.qty,
                            buyingPrice = it.price,
                            bought = it.timestamp.atZone(ZoneId.systemDefault()).toLocalDate(),
                            currentPrice = it.price,
                            lastUpdated = it.timestamp.atZone(ZoneId.systemDefault()).toLocalDate()
                        )
                    }
                }
                is StockSold -> portfolio.remove(it.symbol)
                is StockPriceUpdated -> {
                    val stockinfo = portfolio[it.symbol]!!
                    stockinfo.currentPrice = it.price
                    stockinfo.lastUpdated = it.timestamp.atZone(ZoneId.systemDefault()).toLocalDate()
                }
            }
        }
    }
}

class PortfolioQueryProcessor : MessageProcessor {
    override fun process(input: Message, model: MessageContext): Output {
        val queryModel = model as PortfolioQueryContextModel

        val prices = Portfolio(queryModel.entries.map { Portfolio.Stock(
            qty=it.qty,
            bought= it.bought,
            buyingPrice= it.buyingPrice,
                    currency= it.currency,
            currentPrice= it.currentPrice,
            lastUpdated= it.lastUpdated,
            name=it.name,
            symbol= it.symbol
        ) }.toMutableList())
        val returns = calculateReturns(prices)

        fun result(): PortfolioQueryResult {
            return PortfolioQueryResult(
                portfolioValue = queryModel.entries.map { it.qty*it.currentPrice }.sum(),
                portfolioRateOfReturn = returns.totalRateOfReturn,
                stocks=queryModel.entries.map {
                    PortfolioQueryResult.StockInfo(
                        name=it.name,
                        symbol= it.symbol,
                        currency = it.currency,
                        qty=it.qty,
                        buyingPrice= it.buyingPrice,
                        currentPrice= it.currentPrice,

                        buyingValue= it.qty * it.buyingPrice,
                        currentValue= it.qty * it.currentPrice,

                        returnValue= returns.returns.getValue(it.symbol).returnValue,
                        rateOfReturn= returns.returns.getValue(it.symbol).rateOfReturn,
                    )
                }
            )
        }

        return QueryOutput(result())
    }
}
