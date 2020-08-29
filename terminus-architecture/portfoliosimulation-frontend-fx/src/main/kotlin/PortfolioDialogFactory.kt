package de.muspellheim.portfoliosimulation.frontend.fx

import javafx.fxml.*
import javafx.scene.*
import javafx.stage.*
import javafx.util.*

fun createPortfolioDialog(messageHandling: MessageHandling, stage: Stage = Stage()): Stage {
    val url = PortfolioViewController::class.java.getResource("PortfolioView.fxml")
    val loader = FXMLLoader(url)
    loader.controllerFactory = Callback {
        PortfolioViewController(
            messageHandling,
            onBuy = { onBuyed ->
                val candidateStocksStage = Stage()
                createCandidateStocksDialog(
                    messageHandling,
                    onBuyed = {
                        candidateStocksStage.hide()
                        onBuyed()
                    },
                    stage = candidateStocksStage,
                    owner = stage
                ).show()
            }
        )
    }
    val root = loader.load<Parent>()

    stage.title = "Portfolio Simulation"
    stage.scene = Scene(root, 1280.0, 680.0)
    return stage
}
