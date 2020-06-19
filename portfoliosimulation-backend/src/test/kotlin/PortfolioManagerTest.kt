package de.muspellheim.portfoliosimulation.backend

import de.muspellheim.portfoliosimulation.backend.domain.*
import de.muspellheim.portfoliosimulation.contract.data.domain.*
import org.junit.*
import org.junit.Assert.*
import java.time.*

class PortfolioManagerTest {
    @Test
    fun `calculate returns`() {
        val portfolio = Portfolio(
            mutableListOf(
                Portfolio.Stock(
                    symbol = "S1",
                    qty = 5,
                    buyingPrice = 2.0,
                    currentPrice = 4.0,

                    name = "",
                    bought = LocalDate.now(),
                    currency = "",
                    lastUpdated = LocalDate.now()
                ),
                Portfolio.Stock(
                    symbol = "S2",
                    qty = 10,
                    buyingPrice = 11.0,
                    currentPrice = 16.0,

                    name = "",
                    bought = LocalDate.now(),
                    currency = "",
                    lastUpdated = LocalDate.now()

                )
            )
        )

        val result = calculateReturns(portfolio)

        assertEquals(10.0, result.returns.getValue("S1").returnValue, 0.0)
        assertEquals(1.0, result.returns.getValue("S1").rateOfReturn, 0.0)
        assertEquals(50.0, result.returns.getValue("S2").returnValue, 0.0)
        assertEquals(0.45, result.returns.getValue("S2").rateOfReturn, 0.01)
        assertEquals(60.0, result.totalReturn, 0.0)
        assertEquals(0.5, result.totalRateOfReturn, 0.0)
    }
}
