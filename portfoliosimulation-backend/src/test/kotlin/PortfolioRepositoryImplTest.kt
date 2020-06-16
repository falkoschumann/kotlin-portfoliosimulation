package de.muspellheim.portfoliosimulation.backend

import de.muspellheim.portfoliosimulation.backend.adapters.*
import org.junit.*
import org.junit.Assert.*
import java.time.*

class PortfolioRepositoryImplTest {
    @Test
    fun load() {
        val sut = PortfolioRepositoryImpl("src/test/resources/testportfolio.csv");

        val result = sut.load();

        assertEquals(3, result.entries.count())
        assertEquals("AAPL", result.entries[0].symbol)
        assertEquals(7, result.entries[1].qty)
        assertEquals(LocalDate.of(2018, 9, 15), result.entries[2].bought)
    }

    @Test
    fun `load with no header`() {
        val sut = PortfolioRepositoryImpl("src/test/resources/testportfolio_no_header.csv");

        val result = sut.load();

        assertEquals(3, result.entries.count())
        assertEquals("AAPL", result.entries[0].symbol)
        assertEquals(7, result.entries[1].qty)
        assertEquals(LocalDate.of(2018, 9, 15), result.entries[2].bought)
    }
}
