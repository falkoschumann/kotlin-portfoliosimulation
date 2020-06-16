package de.muspellheim.portfoliosimulation.backend.adapters

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.domain.*
import java.nio.file.*
import java.time.*
import kotlin.streams.*

class PortfolioRepositoryImpl(private val filepath: String = "portfolio.csv") : PortfolioRepository {
    override fun load(): Portfolio {
        val path = Paths.get(filepath)
        if (Files.exists(path).not()) return Portfolio()

        val lines = Files.readAllLines(path)
        if (lines.isEmpty()) return Portfolio()

        val skipHeader = if (lines[0].startsWith("Name")) 1L else 0L
        val records = lines.stream().skip(skipHeader).map { it.split(";") }.toList()
        return Portfolio(entries = records.map(this::mapToEntry))
    }

    private fun mapToEntry(record: List<String>): Portfolio.Stock {
        return Portfolio.Stock(
            name = record[0],
            symbol = record[1],
            currency = record[2],
            bought = LocalDate.parse(record[3]),
            qty = record[4].toInt(),
            buyingPrice = record[5].toDouble(),
            currentPrice = record[6].toDouble(),
            lastUpdated = LocalDate.parse(record[7])
        )
    }

    override fun store(portfolio: Portfolio) {
        TODO("Not yet implemented")
    }
}
