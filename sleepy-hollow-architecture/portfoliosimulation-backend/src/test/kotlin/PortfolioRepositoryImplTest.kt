package de.muspellheim.portfoliosimulation.backend

import de.muspellheim.portfoliosimulation.backend.adapters.*
import de.muspellheim.portfoliosimulation.contract.data.domain.*
import org.junit.*
import org.junit.Assert.*
import java.nio.file.*
import java.time.*

class PortfolioRepositoryImplTest {
    @Test
    fun load() {
        val sut = PortfolioRepositoryImpl("src/test/resources/testportfolio.csv")

        val result = sut.load()

        assertEquals(3, result.entries.count())
        assertEquals("AAPL", result.entries[0].symbol)
        assertEquals(7, result.entries[1].qty)
        assertEquals(LocalDate.of(2018, 9, 15), result.entries[2].bought)
    }

    @Test
    fun `load with no header`() {
        val sut = PortfolioRepositoryImpl("src/test/resources/testportfolio_no_header.csv")

        val result = sut.load()

        assertEquals(3, result.entries.count())
        assertEquals("AAPL", result.entries[0].symbol)
        assertEquals(7, result.entries[1].qty)
        assertEquals(LocalDate.of(2018, 9, 15), result.entries[2].bought)
    }

    @Test
    fun store() {
        Files.copy(
            Paths.get("src/test/resources/testportfolio_no_header.csv"),
            Paths.get("src/test/resources/store.csv"),
            StandardCopyOption.REPLACE_EXISTING
        )
        val sut = PortfolioRepositoryImpl("src/test/resources/store.csv")
        var p = sut.load()

        p.entries[0].qty = 100
        p.entries[1].symbol = "XXX"
        p.entries.add(
            Portfolio.Stock(
                name = "Test",
                symbol = "TTT",
                currency = "USD",
                bought = LocalDate.of(2010, 1, 2),
                qty = 99,
                buyingPrice = 100.0,
                currentPrice = 111.0,
                lastUpdated = LocalDate.of(2019, 5, 2)
            )
        )
        sut.store(p)

        val lines = Files.readAllLines(Paths.get("src/test/resources/store.csv"))
        assertEquals(4, lines.size)
        assertTrue(lines[0].startsWith("Apple"))
        assertTrue(lines[3].startsWith("Test"))
        assertTrue(lines[3].endsWith("2019-05-02"))

        p = sut.load()
        assertEquals(100, p.entries[0].qty)
        assertEquals("XXX", p.entries[1].symbol)
        assertEquals(111.0, p.entries[3].currentPrice, 0.1)
    }
}
