package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.messages.commands.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.*
import javafx.concurrent.*
import javafx.fxml.*
import javafx.scene.*
import javafx.scene.control.*
import javafx.stage.*
import java.text.*
import java.util.concurrent.*

class PortfolioDialog(private val stage: Stage = Stage()) {
    private val portfolioViewController = createPortfolioViewController()
    private val candidateStocksDialog = CandidateStocksDialog(owner = stage)

    var onPortfolioQuery by portfolioViewController::onPortfolioQuery
    var onSellStockCommand by portfolioViewController::onSellStockCommand
    var onUpdatePortfolioCommand by portfolioViewController::onUpdatePortfolioCommand
    var onCandidateStocksQuery by candidateStocksDialog::onCandidateStocksQuery
    lateinit var onBuyStockCommand: (command: BuyStockCommand) -> Unit

    init {
        stage.title = "Portfolio Simulation"
        stage.scene = Scene(portfolioViewController.view, 1280.0, 680.0)

        portfolioViewController.onBuy = {
            candidateStocksDialog.show()
        }
        candidateStocksDialog.onBuyStockCommand = {
            onBuyStockCommand(it)
            candidateStocksDialog.hide()
            onPortfolioQuery(PortfolioQuery())
        }
    }

    fun show() {
        stage.show()
        onPortfolioQuery(PortfolioQuery())
    }

    fun display(portfolio: PortfolioQueryResult) {
        portfolioViewController.display(portfolio)
    }

    fun display(candidateStocks: CandidateStocksQueryResult) {
        candidateStocksDialog.display((candidateStocks))
    }
}

fun createPortfolioViewController(): PortfolioViewController {
    val url = PortfolioViewController::class.java.getResource("PortfolioView.fxml")
    val loader = FXMLLoader(url)
    loader.load<Parent>()
    return loader.getController()
}

class PortfolioViewController {
    lateinit var onPortfolioQuery: (query: PortfolioQuery) -> Unit
    lateinit var onSellStockCommand: (command: SellStockCommand) -> Unit
    lateinit var onUpdatePortfolioCommand: (command: UpdatePortfolioCommand) -> Unit
    lateinit var onBuy: () -> Unit

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
    }

    fun update() {
        ForkJoinPool.commonPool().execute(UpdateTask())
    }

    fun buy() {
        onBuy()
    }

    fun sell() {
        val symbol = stocksTable.selectionModel.selectedItem.symbol
        onSellStockCommand(SellStockCommand(symbol))
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
