package de.muspellheim.portfoliosimulation.backend

import de.muspellheim.portfoliosimulation.backend.adapters.*
import org.junit.*
import org.junit.Assert.*

class StockExchangeProviderImplTest {
    @Test
    fun `get prices`() {
        val sut = StockExchangeProviderImpl()

        val prices = sut.getPrice(listOf("MSFT", "AAPL"))

        assertEquals(2, prices.size)
        assertEquals("MSFT", prices[0].first)
        assertEquals("AAPL", prices[1].first)
        assertTrue(prices[0].second > 0.0)
        assertTrue(prices[1].second > 0.0)
    }
}
