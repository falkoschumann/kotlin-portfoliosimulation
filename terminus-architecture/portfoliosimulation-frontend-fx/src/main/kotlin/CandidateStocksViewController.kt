package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.messages.commands.buystock.BuyStockCommand
import de.muspellheim.portfoliosimulation.contract.messages.queries.candidatestocks.CandidateStocksQuery
import de.muspellheim.portfoliosimulation.contract.messages.queries.candidatestocks.CandidateStocksQueryResult
import de.muspellheim.portfoliosimulation.frontend.fx.util.Action
import javafx.beans.property.SimpleIntegerProperty
import javafx.concurrent.Task
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.util.converter.NumberStringConverter
import java.time.LocalDate
import java.util.concurrent.ForkJoinPool

fun createCandidateStocksViewController(): CandidateStocksViewController {
    val url = PortfolioViewController::class.java.getResource("CandidateStocksView.fxml")
    val loader = FXMLLoader(url)
    loader.load<Parent>()
    return loader.getController()
}

class CandidateStocksViewController {
    val onCandidateStocksQuery = Action<CandidateStocksQuery>()
    val onBuyStockCommand = Action<BuyStockCommand>()

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
