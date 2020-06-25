package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.messages.commands.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*
import javafx.beans.property.*
import javafx.collections.*
import javafx.concurrent.*
import java.text.*
import java.util.concurrent.*

class PortfolioViewModel(private val messageHandling: MessageHandling) {
    private val stocksProperty = ReadOnlyObjectWrapper<ObservableList<StockInfoModel>>()
    fun stocksProperty() = stocksProperty.readOnlyProperty
    var stocks: ObservableList<StockInfoModel>
        get() = stocksProperty.get()
        private set(value) = stocksProperty.set(value)

    private val updatingProperty = ReadOnlyBooleanWrapper()
    fun updatingProperty() = updatingProperty.readOnlyProperty
    var updating: Boolean
        get() = updatingProperty.get()
        private set(value) = updatingProperty.set(value)

    private val portfolioValueProperty = ReadOnlyStringWrapper()
    fun portfolioValueProperty() = portfolioValueProperty.readOnlyProperty
    var portfolioValue: String
        get() = portfolioValueProperty.get()
        private set(value) = portfolioValueProperty.set(value)

    private val portfolioRateOfReturnProperty = ReadOnlyStringWrapper()
    fun portfolioRateOfReturnProperty() = portfolioRateOfReturnProperty.readOnlyProperty
    var portfolioRateOfReturn: String
        get() = portfolioRateOfReturnProperty.get()
        private set(value) = portfolioRateOfReturnProperty.set(value)

    init {
        val portfolio = messageHandling.handle(PortfolioQuery())
        display(portfolio)
    }

    fun update() {
        ForkJoinPool.commonPool().execute(UpdateTask())
    }

    fun buy() {
        // TODO()
    }

    private fun display(portfolio: PortfolioQueryResult) {
        val stockInfoModel = portfolio.stocks.map { mapStockInfo(it) }
        stocks = FXCollections.observableList(stockInfoModel)
        portfolioValue = formatCurrency(portfolio.portfolioValue)
        portfolioRateOfReturn = formatCurrency(portfolio.portfolioRateOfReturn)
    }

    private inner class UpdateTask : Task<PortfolioQueryResult>() {
        init {
            stocks = FXCollections.emptyObservableList()
            updating = true
        }

        override fun call(): PortfolioQueryResult {
            messageHandling.handle(UpdatePortfolioCommand())
            return messageHandling.handle(PortfolioQuery())
        }

        override fun succeeded() {
            display(value)
            updating = false
        }
    }
}

fun mapStockInfo(stock: PortfolioQueryResult.StockInfo) = StockInfoModel(
    name = stock.name,
    symbol = stock.symbol,
    currency = stock.currency,
    qty = stock.qty,
    buyingPrice = formatCurrency(stock.buyingPrice),
    buyingValue = formatCurrency(stock.buyingValue),
    currentPrice = formatCurrency(stock.currentPrice),
    currentValue = formatCurrency(stock.currentValue),
    returnValue = formatCurrency(stock.returnValue),
    rateOfReturn = formatCurrency(stock.rateOfReturn)
)

fun formatCurrency(value: Double): String {
    val format = NumberFormat.getNumberInstance()
    format.minimumFractionDigits = 2
    format.maximumFractionDigits = 2
    return format.format(value)
}

data class StockInfoModel(
    val name: String,
    val symbol: String,
    val currency: String,
    val qty: Int,
    val buyingPrice: String,
    val buyingValue: String,
    val currentPrice: String,
    val currentValue: String,
    val returnValue: String,
    val rateOfReturn: String
)
