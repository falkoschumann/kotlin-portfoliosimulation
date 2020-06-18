package de.muspellheim.portfoliosimulation.backend.adapters.alphavantage

import java.io.*
import java.net.*
import java.net.http.*
import javax.json.*

class AlphaVantageProviderImpl : AlphaVantageProvider {
    private lateinit var baseUrl: String

    init {
        val secretsFilename = "/alphavantage.secrets"
        javaClass.getResourceAsStream(secretsFilename).use {
            val reader = Json.createReader(it)
            val secrets = reader.readObject().getString("APIkey")
            baseUrl = "https://www.alphavantage.co/query?apikey=${secrets}"
        }
    }

    override fun getQuote(symbol: String): StockPrice {
        val endpointUrl = "${baseUrl}&function=GLOBAL_QUOTE&symbol=${symbol}"
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder(URI(endpointUrl)).GET().build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val quoteText = response.body()
        if (response.statusCode() != 200) throw Exception(quoteText)
        val quote = Json.createReader(StringReader(quoteText)).readObject()
        val quoteObj =
            quote["Global Quote"]?.asJsonObject() ?: throw Exception(quote["Note"].toString())
        return StockPrice(
            quoteObj.getString("01. symbol"),
            quoteObj.getString("05. price").toDouble()
        )
    }

    override fun getConversionRateToEuro(fromCurrencyAbbreviation: String): ExchangeRateToEuro {
        TODO("Not yet implemented")
    }

    override fun findMatchingStocks(pattern: String): List<StockInfo> {
        TODO("Not yet implemented")
    }
}
