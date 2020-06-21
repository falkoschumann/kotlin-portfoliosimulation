package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*
import javafx.beans.property.*
import javafx.collections.*

class MainViewController(private val messageHandling: MessageHandling) {
    private val stocksProperty = ReadOnlyObjectWrapper<ObservableList<PortfolioQueryResult.StockInfo>>()
    fun stocksProperty() = stocksProperty.readOnlyProperty
    var stocks: ObservableList<PortfolioQueryResult.StockInfo>
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
        stocks = FXCollections.observableList(portfolio.stocks)
        portfolioValue = portfolio.portfolioValue.toString()
        portfolioRateOfReturn = portfolio.portfolioRateOfReturn.toString()
    }
}
