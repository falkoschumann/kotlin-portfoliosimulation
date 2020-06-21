package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*
import javafx.beans.property.*
import javafx.collections.*
import java.text.*

class MainViewModel(private val messageHandling: MessageHandling) {
    private val stocksProperty = ReadOnlyObjectWrapper<ObservableList<StockInfoModel>>()
    fun stocksProperty() = stocksProperty.readOnlyProperty
    var stocks: ObservableList<StockInfoModel>
        get() = stocksProperty.get()
        private set(value) = stocksProperty.set(value)


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

    private fun display(portfolio: PortfolioQueryResult) {
        val stockInfoModel = portfolio.stocks.map { mapStockInfo(it) }
        stocks = FXCollections.observableList(stockInfoModel)
        portfolioValue = formatCurrency(portfolio.portfolioValue)
        portfolioRateOfReturn = formatCurrency(portfolio.portfolioRateOfReturn)
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