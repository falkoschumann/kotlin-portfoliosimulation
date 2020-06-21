package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.*
import javafx.fxml.*
import javafx.scene.*
import javafx.util.*

fun createMainView(messageHandling: MessageHandling): Parent {
    val url = MainViewModel::class.java.getResource("MainView.fxml")
    val loader = FXMLLoader(url)
    loader.controllerFactory = Callback { MainViewModel(messageHandling) }
    return loader.load<Parent>()
}
