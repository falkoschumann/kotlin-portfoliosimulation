package de.muspellheim.portfoliosimulation.backend.adapters.alphavantage

import java.io.*
import java.net.*
import java.net.http.*
import javax.json.*

class AlphaVantageProviderImpl : AlphaVantageProvider {
    private var baseUrl: String

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
            quote["Global Quote"]?.asJsonObject() ?: throw IOException(quote.toString())
        return StockPrice(
            quoteObj.getString("01. symbol"),
            quoteObj.getString("05. price").toDouble()
        )
    }

    override fun getConversionRateToEuro(fromCurrencyAbbreviation: String): ExchangeRateToEuro {
        TODO("Not yet implemented")
    }

    override fun findMatchingStocks(pattern: String): List<StockInfo> {
        val endpointUrl = "${baseUrl}&function=SYMBOL_SEARCH&keywords=${pattern}"
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder(URI(endpointUrl)).GET().build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val queryResultText = response.body()
        if (response.statusCode() != 200) throw Exception(queryResultText)
        val quote = Json.createReader(StringReader(queryResultText)).readObject()
        val quoteResultObj =
            quote["bestMatches"]?.asJsonArray() ?: throw IOException(quote.toString())

        val stockInfos = mutableListOf<StockInfo>()
        for (item in quoteResultObj) {
            val itemProperties = item.asJsonObject()
            val info = StockInfo(
                symbol = itemProperties.getString("1. symbol"),
                name = itemProperties.getString("2. name"),
                region = itemProperties.getString("4. region"),
                currency = itemProperties.getString("8. currency")
            )
            stockInfos.add(info)
        }
        return stockInfos.toList()
    }
}
