package de.muspellheim.portfoliosimulation.fx

import de.muspellheim.portfoliosimulation.backend.*
import de.muspellheim.portfoliosimulation.frontend.fx.*
import javafx.application.*
import javafx.scene.*
import javafx.stage.*

class AppFx : Application() {
    override fun start(stage: Stage) {
        val backend = MessageHandlingImpl()
        val frontend = createMainView(backend)
        stage.scene = Scene(frontend, 1280.0, 680.0)
        stage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(AppFx::class.java, *args)
}
