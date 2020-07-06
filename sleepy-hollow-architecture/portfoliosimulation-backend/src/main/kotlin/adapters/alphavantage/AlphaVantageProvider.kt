package de.muspellheim.portfoliosimulation.backend.adapters.alphavantage

interface AlphaVantageProvider {
    fun getQuote(symbol: String): StockPrice
    fun getConversionRateToEuro(fromCurrencyAbbreviation: String): ExchangeRateToEuro
    fun findMatchingStocks(pattern: String): List<StockInfo>
}
