package de.muspellheim.portfoliosimulation.backend.adapters.alphavantage

import javax.json.*

class AlphaVantageProviderImpl : AlphaVantageProvider {
    private lateinit var baseUrl: String

    init {
        val secretsFilename = "/alphavantage.secrets"
        javaClass.getResourceAsStream(secretsFilename).use {
            val parser = Json.createParser(it)
            val secrets = parser.`object`.getString("APIkey")
            baseUrl = "https://www.alphavantage.co/query?apikey=${secrets}"
        }
    }

    override fun getQuote(symbol: String): StockPrice {
        TODO("Not yet implemented")
    }

    override fun getConversionRateToEuro(fromCurrencyAbbreviation: String): ExchangeRateToEuro {
        TODO("Not yet implemented")
    }

    override fun findMatchingStocks(pattern: String): List<StockInfo> {
        TODO("Not yet implemented")
    }
}
