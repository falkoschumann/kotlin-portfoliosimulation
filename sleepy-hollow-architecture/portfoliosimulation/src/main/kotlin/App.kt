package de.muspellheim.portfoliosimulation

import de.muspellheim.portfoliosimulation.backend.*
import de.muspellheim.portfoliosimulation.frontend.*

fun main(args: Array<String>) {
    val backend = MessageHandlingImpl()
    val frontend = UserInterface(backend)
    frontend.run()
}
