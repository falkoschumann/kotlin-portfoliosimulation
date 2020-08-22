package de.muspellheim.portfoliosimulation

import de.muspellheim.portfoliosimulation.backend.*
import de.muspellheim.portfoliosimulation.frontend.*

fun main() {
    val backend = MessageHandlingImpl()
    val frontend = UserInterface(backend)
    frontend.run()
}
