package de.muspellheim.portfoliosimulation.backend.messagepipelines.commands

import de.muspellheim.portfoliosimulation.messagehandling.pipeline.messagecontext.*

open class PortfoliosymbolsContextModel(val values: Set<String>) : MessageContext
