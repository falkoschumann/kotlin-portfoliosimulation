package de.muspellheim.portfoliosimulation.backend.domain

import de.muspellheim.portfoliosimulation.contract.data.domain.*

data class PortfolioReturns(
    val returns: Map<String, EntryReturn>,
    val totalReturn: Double,
    val totalRateOfReturn: Double
) {
    data class EntryReturn(val returnValue: Double, val rateOfReturn: Double)
}

fun calculateReturns(portfolio: Portfolio): PortfolioReturns {
    val returns: Map<String, PortfolioReturns.EntryReturn> = portfolio.entries.associateBy(
        keySelector = { it.symbol },
        valueTransform = {
            val buyingValue = it.qty * it.buyingPrice
            val returnValue = it.qty * it.currentPrice - buyingValue
            PortfolioReturns.EntryReturn(
                returnValue,
                rateOfReturn = returnValue / buyingValue
            )
        }
    )

    val totalReturn = returns.values.map { it.returnValue }.sum()
    val totalBuyingValue = portfolio.entries.map { it.qty * it.buyingPrice }.sum()
    return PortfolioReturns(
        returns,
        totalReturn,
        totalRateOfReturn = if (totalBuyingValue > 0.0) totalReturn / totalBuyingValue else 0.0
    )
}
