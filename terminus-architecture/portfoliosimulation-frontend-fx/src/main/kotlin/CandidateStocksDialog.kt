package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.messages.commands.buystock.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.candidatestocks.*
import javafx.beans.property.*
import javafx.concurrent.*
import javafx.fxml.*
import javafx.scene.*
import javafx.scene.control.*
import javafx.stage.*
import javafx.util.converter.*
import java.time.*
import java.util.concurrent.*

class CandidateStocksDialog(private val stage: Stage = Stage(), owner: Window) {
    private val candidateStocksViewController = createCandidateStocksViewController()

    var onCandidateStocksQuery by candidateStocksViewController::onCandidateStocksQuery
    var onBuyStockCommand by candidateStocksViewController::onBuyStockCommand

    init {
        stage.initOwner(owner)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.title = "Candidate Stocks"
        stage.scene = Scene(candidateStocksViewController.view, 700.0, 400.0)
    }

    fun show() {
        candidateStocksViewController.reset()
        stage.show()
    }

    fun hide() {
        stage.hide()
    }

    fun display(candidateStocks: CandidateStocksQueryResult) {
        candidateStocksViewController.display((candidateStocks))
    }
}

fun createCandidateStocksViewController(): CandidateStocksViewController {
    val url = PortfolioViewController::class.java.getResource("CandidateStocksView.fxml")
    val loader = FXMLLoader(url)
    loader.load<Parent>()
    return loader.getController()
}

class CandidateStocksViewController {
    lateinit var onCandidateStocksQuery: (query: CandidateStocksQuery) -> Unit
    lateinit var onBuyStockCommand: (command: BuyStockCommand) -> Unit

    lateinit var view: Parent
    lateinit var identificationText: TextField
    lateinit var placeholderLabel: Label
    lateinit var searchButton: Button
    lateinit var candidatesTable: TableView<CandidateStocksQueryResult.CandidateStock>
    lateinit var searchingLabel: Label
    lateinit var qtyText: TextField
    lateinit var buyButton: Button

    private val qtyProperty = SimpleIntegerProperty(1)

    fun initialize() {
        val candidateSelected = candidatesTable.selectionModel.selectedItemProperty().isNotNull
        qtyText.textProperty().bindBidirectional(qtyProperty, NumberStringConverter())
        val qtyValid = qtyProperty.greaterThan(0)
        buyButton.disableProperty().bind(candidateSelected.not().or(qtyValid.not()))
    }

    fun reset() {
        candidatesTable.items.clear()
    }

    fun search() {
        ForkJoinPool.commonPool().execute(SearchTask())
    }

    fun buy() {
        val toBuy = candidatesTable.selectionModel.selectedItem
        onBuyStockCommand(
                BuyStockCommand(
                        stockName = toBuy.name,
                        stockSymbol = toBuy.symbol,
                        stockPriceCurrency = toBuy.currency,
                        qty = qtyProperty.value,
                        stockPrice = toBuy.price,
                        bought = LocalDate.now()
                )
        )
    }

    fun display(candidateStocks: CandidateStocksQueryResult) {
        candidatesTable.items.setAll(candidateStocks.candidates)
        searchButton.isDisable = false
        searchingLabel.isVisible = false
        placeholderLabel.isVisible = true
    }

    private inner class SearchTask : Task<Unit>() {
        init {
            candidatesTable.items.clear()
            placeholderLabel.isVisible = false
            placeholderLabel.text = "No candidates found!"
            searchButton.isDisable = true
            searchingLabel.isVisible = true
        }

        override fun call() {
            onCandidateStocksQuery(CandidateStocksQuery(identificationText.text))
        }
    }
}
