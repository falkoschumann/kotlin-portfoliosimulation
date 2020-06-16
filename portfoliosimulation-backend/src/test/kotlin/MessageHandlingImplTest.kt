package de.muspellheim.portfoliosimulation.backend

import de.muspellheim.portfoliosimulation.backend.adapters.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*
import org.junit.*
import org.junit.Assert.*

class MessageHandlingImplTest {
    @Test
    fun `portfolio query`() {
        val repo = PortfolioRepositoryImpl("src/test/resources/smallportfolio.csv")
        val sut = MessageHandlingImpl(repo)

        val result = sut.handle(PortfolioQuery())

        assertEquals(180.0, result.portfolioValue, 0.0)
        assertEquals(0.5, result.portfolioRateOfReturn, 0.0)
        assertEquals(20.0, result.stocks[0].currentValue, 0.0)
        assertEquals(0.45, result.stocks[1].rateOfReturn, 0.01)
    }
}
