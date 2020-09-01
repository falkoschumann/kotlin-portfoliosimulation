package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.messages.commands.sellstock.*
import de.muspellheim.portfoliosimulation.contract.messages.commands.updateportfolio.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.portfolio.*
import de.muspellheim.portfoliosimulation.frontend.fx.util.*
import javafx.concurrent.*
import javafx.fxml.*
import javafx.scene.*
import javafx.scene.control.*
import java.text.*
import java.util.concurrent.*

fun createPortfolioViewController(): PortfolioViewController {
    val url = PortfolioViewController::class.java.getResource("PortfolioView.fxml")
    val loader = FXMLLoader(url)
    loader.load<Parent>()
    return loader.getController()
}

class PortfolioViewController {
    val onPortfolioQuery = Action<PortfolioQuery>()
    val onSellStockCommand = Action<SellStockCommand>()
    val onUpdatePortfolioCommand = Action<UpdatePortfolioCommand>()

    private val onBuy: (onBuyed: () -> Unit) -> Unit

    lateinit var view: Parent
    lateinit var updateButton: Button
    lateinit var sellButton: Button
    lateinit var placeholderLabel: Label
    lateinit var updatingLabel: Label
    lateinit var stocksTable: TableView<StockInfoModel>
    lateinit var portfolioValueText: TextField
    lateinit var portfolioRateOfReturnText: TextField

    fun initialize() {
        sellButton.disableProperty().bind(stocksTable.selectionModel.selectedItemProperty().isNull)
        refresh()
    }

    fun update() {
        ForkJoinPool.commonPool().execute(UpdateTask())
    }

    fun buy() {
        onBuy { refresh() }
    }

    fun sell() {
        val symbol = stocksTable.selectionModel.selectedItem.symbol
        onSellStockCommand(SellStockCommand(symbol))
        refresh()
    }

    private fun refresh() {
        onPortfolioQuery(PortfolioQuery())
    }

    fun display(portfolio: PortfolioQueryResult) {
        val stockInfoModel = portfolio.stocks.map { mapStockInfo(it) }
        stocksTable.items.setAll(stockInfoModel)
        portfolioValueText.text = formatCurrency(portfolio.portfolioValue)
        portfolioRateOfReturnText.text = formatCurrency(portfolio.portfolioRateOfReturn)
    }

    private inner class UpdateTask : Task<Unit>() {
        init {
            stocksTable.items.clear()
            placeholderLabel.isVisible = false
            updateButton.isDisable = true
            updatingLabel.isVisible = true
        }

        override fun call() {
            onUpdatePortfolioCommand(UpdatePortfolioCommand())
        }

        override fun succeeded() {
            updateButton.isDisable = false
            updatingLabel.isVisible = false
            placeholderLabel.isVisible = true
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
