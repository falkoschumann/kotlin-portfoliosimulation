package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*
import javafx.concurrent.*
import javafx.scene.control.*
import java.util.concurrent.*

class CandidateStocksViewController constructor(private val messageHandling: MessageHandling) {
    lateinit var identificationText: TextField
    lateinit var placeholderLabel: Label
    lateinit var searchButton: Button
    lateinit var candidatesTable: TableView<CandidateStocksQueryResult.CandidateStock>
    lateinit var searchingLabel: Label

    fun search() {
        ForkJoinPool.commonPool().execute(SearchTask())
    }

    fun buy() {
        TODO()
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
