package de.muspellheim.portfoliosimulation.backend.messagepipelines.commands

import de.muspellheim.portfoliosimulation.backend.events.*
import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.messagehandling.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.*

open class PortfoliosymbolsContextManager(
    es: EventStore,
    private val contextModelType: Class<out PortfoliosymbolsContextModel>
) : MessageContextManager {
    private val symbolsInPortfolio = mutableSetOf<String>()

    init {
        update(es.replay(StockBought::class.java, StockSold::class.java))
    }

    override fun load(input: Message): MessageContext {
        return contextModelType.getDeclaredConstructor(Set::class.java).newInstance(symbolsInPortfolio)
    }

    final override fun update(events: Iterable<Event>) {
        events.forEach {
            when (it) {
                is StockBought -> symbolsInPortfolio.add(it.symbol)
                is StockSold -> symbolsInPortfolio.remove(it.symbol)
            }
        }
    }
}
