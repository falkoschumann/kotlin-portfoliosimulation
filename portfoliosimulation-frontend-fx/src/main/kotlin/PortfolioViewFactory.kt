package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.*
import javafx.fxml.*
import javafx.scene.*
import javafx.util.*

fun createPortfolioView(messageHandling: MessageHandling): Parent {
    val url = PortfolioViewModel::class.java.getResource("PortfolioView.fxml")
    val loader = FXMLLoader(url)
    loader.controllerFactory = Callback { PortfolioViewModel(messageHandling) }
    return loader.load<Parent>()
}
