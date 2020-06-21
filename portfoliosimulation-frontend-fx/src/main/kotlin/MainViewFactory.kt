package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*
import javafx.beans.value.*
import javafx.fxml.*
import javafx.scene.*
import javafx.scene.control.*
import javafx.scene.control.cell.*
import javafx.util.*

fun createMainView(messageHandling: MessageHandling): Parent {
    val url = MainViewController::class.java.getResource("MainView.fxml")
    val loader = FXMLLoader(url)
    loader.controllerFactory = Callback { MainViewController(messageHandling) }
    return loader.load<Parent>()
}
