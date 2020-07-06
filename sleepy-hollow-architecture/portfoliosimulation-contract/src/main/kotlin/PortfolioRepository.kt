package de.muspellheim.portfoliosimulation.contract

import de.muspellheim.portfoliosimulation.contract.data.domain.*

interface PortfolioRepository {
    fun load(): Portfolio
    fun store(portfolio: Portfolio)
}
