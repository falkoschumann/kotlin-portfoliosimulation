package de.muspellheim.portfoliosimulation.contract

import de.muspellheim.portfoliosimulation.contract.data.messages.commands.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*

interface MessageHandling {
    fun handle(command: UpdatePortfolioCommand): CommandStatus
    fun handle(query: PortfolioQuery): PortfolioQueryResult
    fun handle(command: BuyStockCommand): CommandStatus
    fun handle(query: CandidateStocksQuery): CandidateStocksQueryResult
    fun handle(command: SellStockCommand): CommandStatus
    fun handle(query: PortfolioStockQuery): PortfolioStockQueryResult
}
