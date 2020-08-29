package de.muspellheim.portfoliosimulation.frontend.fx

import de.muspellheim.portfoliosimulation.contract.*
import javafx.fxml.*
import javafx.scene.*
import javafx.stage.*
import javafx.util.*

fun createCandidateStocksDialog(
    messageHandling: MessageHandling,
    onBuyed: () -> Unit,
    stage: Stage = Stage(),
    owner: Window? = null
): Stage {
    val url = CandidateStocksViewController::class.java.getResource("CandidateStocksView.fxml")
    val loader = FXMLLoader(url)
    loader.controllerFactory = Callback { CandidateStocksViewController(messageHandling, onBuyed) }
    val root = loader.load<Parent>()

    stage.initOwner(owner)
    stage.initModality(Modality.WINDOW_MODAL)
    stage.title = "Candidate Stocks"
    stage.scene = Scene(root, 700.0, 400.0)
    return stage
}
