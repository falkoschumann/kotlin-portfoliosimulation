package de.muspellheim.portfoliosimulation.frontend

import de.muspellheim.portfoliosimulation.contract.*
import de.muspellheim.portfoliosimulation.contract.data.messages.commands.*
import de.muspellheim.portfoliosimulation.contract.data.messages.queries.*
import java.time.*

class UserInterface(private val mh: MessageHandling) {
    fun run() {
        val portfolio = mh.handle(PortfolioQuery())
        display(portfolio)
        menuLoop()
    }

    private fun menuLoop() {
        loop@ while (true) {
            println(">>> (D)isplay, (B)uy, (S)ell, (U)pdate, E(x)it?: ")
            when (readLine()?.toUpperCase()) {
                "X" -> return
                "D" -> {
                    val portfolio = mh.handle(PortfolioQuery())
                    display(portfolio)
                }
                "U" -> {
                    mh.handle(UpdatePortfolioCommand())
                    val portfolio = mh.handle(PortfolioQuery())
                    display(portfolio)
                }
                "B" -> {
                    println("Identification?: ")
                    var input = readLine()!!
                    if (input == "") continue@loop

                    val candidates = mh.handle(CandidateStocksQuery(input))
                    displayBuyCandidates(candidates.candidates)

                    println("Index of stock to buy?: ")
                    input = readLine()!!
                    if (input == "") continue@loop

                    val index = input.toInt() - 1
                    val toBuy = candidates.candidates[index]
                    displayChosenCandidate(toBuy)

                    println("Buy qty?: ")
                    input = readLine()!!
                    if (input == "") continue@loop

                    val qty = input.toInt()

                    mh.handle(
                        BuyStockCommand(
                            stockName = toBuy.name,
                            stockSymbol = toBuy.symbol,
                            stockPriceCurrency = toBuy.currency,
                            qty = qty,
                            stockPrice = toBuy.price,
                            bought = LocalDate.now()
                        )
                    )
                }
                "S" -> {
                    TODO()
                }
                null -> return
            }
        }
    }

    private fun displayChosenCandidate(candidate: CandidateStocksQueryResult.CandidateStock) {
        println("${candidate.name} (${candidate.symbol})")
        println("${candidate.price} ${candidate.currency}")
    }

    private fun displayBuyCandidates(candidates: List<CandidateStocksQueryResult.CandidateStock>) {
        for (i in candidates.indices) {
            println("${i + 1}. ${candidates[i].name} (${candidates[i].symbol}): ${candidates[i].price} ${candidates[i].currency}")
        }
    }

    private fun display(portfolio: PortfolioQueryResult) {
        if (portfolio.stocks.isEmpty()) {
            println("Empty portfolio!")
            return
        }

        for (i in portfolio.stocks.indices) {
            val s = portfolio.stocks[i]
            println("${i + 1}. ${s.name} (${s.symbol}), bought: ${s.qty}x${s.buyingPrice}=${s.qty * s.buyingPrice}, curr.: ${s.qty}x${s.currentPrice}=${s.qty * s.currentPrice} -> ${s.returnValue} / ${s.rateOfReturn}")
        }
        println("Portfolio value: ${portfolio.portfolioValue} / ${portfolio.portfolioRateOfReturn}")
    }
}
