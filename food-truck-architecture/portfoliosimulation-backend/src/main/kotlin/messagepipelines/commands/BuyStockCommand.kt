package de.muspellheim.portfoliosimulation.backend.messagepipelines.commands

import de.muspellheim.portfoliosimulation.backend.events.*
import de.muspellheim.portfoliosimulation.contract.messages.commands.*
import de.muspellheim.portfoliosimulation.eventstore.*
import de.muspellheim.portfoliosimulation.messagehandling.*
import de.muspellheim.portfoliosimulation.messagehandling.pipeline.*
import java.time.*

class BuyStockCommandContextModel : MessageContext

class BuyStockCommandContextManager : MessageContextManager {
    override fun load(input: Message): MessageContext {
        return BuyStockCommandContextModel()
    }

    override fun update(events: Iterable<Event>) {}
}

class BuyStockCommandProcessor : MessageProcessor {
    override fun process(input: Message, model: MessageContext): Output {
        val cmd = input as BuyStockCommand
        return CommandOutput(
            Success,
            listOf(
                StockBought(
                    name = cmd.stockName,
                    symbol = cmd.stockSymbol,
                    currency = cmd.stockPriceCurrency,
                    qty = cmd.qty,
                    price = cmd.stockPrice,
                    timestamp = LocalDateTime.of(cmd.bought, LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()
                ),
                StockPriceUpdated(
                    symbol = cmd.stockSymbol,
                    price = cmd.stockPrice
                )
            )
        )
    }
}
