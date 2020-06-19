package de.muspellheim.portfoliosimulation.backend

import de.muspellheim.portfoliosimulation.backend.adapters.*
import de.muspellheim.portfoliosimulation.contract.data.messages.commands.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*
import org.junit.*
import org.junit.Assert.*
import java.nio.file.*

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

    @Test
    //@Ignore("Needs online access")
    fun `update portfolio`() {
        Files.copy(
            Paths.get("src/test/resources/portfoliotoupdate.csv"),
            Paths.get("src/test/resources/updateportfolio.csv"),
            StandardCopyOption.REPLACE_EXISTING
        )
        val repo = PortfolioRepositoryImpl("src/test/resources/updateportfolio.csv")
        val before = repo.load()
        val sut = MessageHandlingImpl(repo)

        sut.handle(UpdatePortfolioCommand())

        val after = repo.load()
        for (i in before.entries.indices) {
            assertTrue(after.entries[i].currentPrice > 0)
            assertTrue(before.entries[i].currentPrice != after.entries[i].currentPrice)
            assertTrue(before.entries[i].lastUpdated < after.entries[i].lastUpdated)
        }
    }

    @Test
    //@Ignore("Requires access to online service")
    fun `find candidates`() {
        val ex = StockExchangeProviderImpl()
        val sut = MessageHandlingImpl(ex = ex)

        val result = sut.handle(CandidateStocksQuery(pattern = "Apple"))

        assertTrue(result.candidates.isNotEmpty())

        println("Candidates found: ${result.candidates.size}")
        for (r in result.candidates) {
            println("${r.name} (${r.symbol}): ${r.price} ${r.currency}")
        }
    }
}
