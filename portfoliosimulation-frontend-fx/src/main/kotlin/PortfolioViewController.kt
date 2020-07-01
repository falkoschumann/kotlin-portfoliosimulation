package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.messages.commands.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*
import javafx.concurrent.*
import javafx.scene.control.*
import java.text.*
import java.util.concurrent.*

class PortfolioViewController(
    private val messageHandling: MessageHandling,
    private val onBuy: (onBuyed: () -> Unit) -> Unit
) {
    lateinit var updateButton: Button
    lateinit var placeholderLabel: Label
    lateinit var updatingLabel: Label
    lateinit var stocksTable: TableView<StockInfoModel>
    lateinit var portfolioValueText: TextField
    lateinit var portfolioRateOfReturnText: TextField

    fun initialize() {
        refresh()
    }

    fun update() {
        ForkJoinPool.commonPool().execute(UpdateTask())
    }

    fun buy() {
        onBuy { refresh() }
    }

    private fun refresh() {
        val portfolio = messageHandling.handle(PortfolioQuery())
        display(portfolio)
    }

    private fun display(portfolio: PortfolioQueryResult) {
        val stockInfoModel = portfolio.stocks.map { mapStockInfo(it) }
        stocksTable.items.setAll(stockInfoModel)
        portfolioValueText.text = formatCurrency(portfolio.portfolioValue)
        portfolioRateOfReturnText.text = formatCurrency(portfolio.portfolioRateOfReturn)
    }

    private inner class UpdateTask : Task<PortfolioQueryResult>() {
        init {
            stocksTable.items.clear()
            placeholderLabel.isVisible = false
            updateButton.isDisable = true
            updatingLabel.isVisible = true
        }

        override fun call(): PortfolioQueryResult {
            messageHandling.handle(UpdatePortfolioCommand())
            return messageHandling.handle(PortfolioQuery())
        }

        override fun succeeded() {
            display(value)
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
