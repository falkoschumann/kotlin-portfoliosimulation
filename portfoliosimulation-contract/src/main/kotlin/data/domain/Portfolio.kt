package de.muspellheim.portfoliosimulation.contract.data.domain

import java.time.*

data class Portfolio(val entries: List<Stock> = emptyList()) {
    data class Stock(
        val name: String,
        val symbol: String,
        val currency: String,
        val bought: LocalDate,
        val qty: Int,
        val buyingPrice: Double,
        val currentPrice: Double,
        val lastUpdated: LocalDate
    )
}
