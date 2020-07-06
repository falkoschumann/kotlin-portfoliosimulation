package de.muspellheim.portfoliosimulation.contract.data.domain

import java.time.*

data class Portfolio(val entries: MutableList<Stock> = mutableListOf()) {
    val stockSymbols: List<String>
        get() = entries.map { it.symbol }.distinct()

    fun find(pattern: String): List<Stock> {
        return entries.filter { it.name.contains(pattern, true) || it.symbol.contains(pattern, true) }
    }

    fun remove(symbol: String) {
        entries.removeIf { it.symbol == symbol }
    }

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
        var symbol: String,
        val currency: String,
        val bought: LocalDate,
        var qty: Int,
        val buyingPrice: Double,
        var currentPrice: Double,
        var lastUpdated: LocalDate
    )
}
