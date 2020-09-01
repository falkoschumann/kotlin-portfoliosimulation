package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.data.messages.commands.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*
import javafx.beans.property.*
import javafx.concurrent.*
import javafx.scene.control.*
import javafx.util.converter.*
import java.time.*
import java.util.concurrent.*

class CandidateStocksViewController constructor(
        private val messageHandling: MessageHandling,
        private val onBuyed: () -> Unit
) {
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
        messageHandling.handle(
                BuyStockCommand(
                        stockName = toBuy.name,
                        stockSymbol = toBuy.symbol,
                        stockPriceCurrency = toBuy.currency,
                        qty = qtyProperty.value,
                        stockPrice = toBuy.price,
                        bought = LocalDate.now()
                )
        )
        onBuyed()
    }

    private fun displayBuyCandidates(candidateStocks: CandidateStocksQueryResult) {
        candidatesTable.items.setAll(candidateStocks.candidates)
    }

    private inner class SearchTask : Task<CandidateStocksQueryResult>() {
        init {
            candidatesTable.items.clear()
            placeholderLabel.isVisible = false
            placeholderLabel.text = "No candidates found!"
            searchButton.isDisable = true
            searchingLabel.isVisible = true
        }

        override fun call(): CandidateStocksQueryResult {
            return messageHandling.handle(CandidateStocksQuery(identificationText.text))
        }

        override fun succeeded() {
            displayBuyCandidates(value)
            searchButton.isDisable = false
            searchingLabel.isVisible = false
            placeholderLabel.isVisible = true
        }
    }
}
