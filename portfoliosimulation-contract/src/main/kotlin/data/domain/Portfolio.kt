package de.muspellheim.portfoliosimulation.contract.data.domain

import java.time.*

data class Portfolio(val entries: List<Stock> = emptyList()) {
    val stockSymbols: List<String>
        get() = entries.map { it.symbol }.distinct()

    fun update(prices: List<Pair<String, Double>>) {
        prices.forEach { update(it.first, it.second) }
    }

    fun update(symbol: String, price: Double) {
        val stock = entries.find { it.symbol == symbol } ?: return

        stock.currentPrice = price
        stock.lastUpdated = LocalDate.now()
    }

    data class Stock(
        val name: String,
        val symbol: String,
        val currency: String,
        val bought: LocalDate,
        val qty: Int,
        val buyingPrice: Double,
        var currentPrice: Double,
        var lastUpdated: LocalDate
    )
}
