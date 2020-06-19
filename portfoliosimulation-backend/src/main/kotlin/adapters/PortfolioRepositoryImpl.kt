package de.muspellheim.portfoliosimulation.backend.adapters

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.domain.*
import java.nio.file.*
import java.time.*
import java.time.format.*
import kotlin.streams.*

class PortfolioRepositoryImpl(private val filepath: String = "portfolio.csv") :
    PortfolioRepository {
    override fun load(): Portfolio {
        val path = Paths.get(filepath)
        if (Files.exists(path).not()) return Portfolio()

        val lines = Files.readAllLines(path)
        if (lines.isEmpty()) return Portfolio()

        val skipHeader = if (lines[0].startsWith("Name")) 1L else 0L
        val records = lines.stream().skip(skipHeader).map { it.split(";") }.toList()
        return Portfolio(entries = records.map(this::mapToEntry).toMutableList())
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
        val lines = portfolio.entries.map(this::mapToLine)
        val path = Paths.get(filepath)
        Files.write(path, lines)
    }

    private fun mapToLine(e: Portfolio.Stock): String {
        return "${e.name};${e.symbol};${e.currency};${e.bought.format(DateTimeFormatter.ISO_DATE)};${e.qty};${e.buyingPrice};${e.currentPrice};${e.lastUpdated.format(
            DateTimeFormatter.ISO_DATE
        )}"
    }
}
