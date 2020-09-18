package de.muspellheim.portfoliosimulation.frontend

import de.muspellheim.portfoliosimulation.contract.messages.commands.*
import de.muspellheim.portfoliosimulation.contract.messages.queries.*
import de.muspellheim.portfoliosimulation.frontend.util.*
import java.time.*

class UserInterface {
    val onPortfolioQuery = Action<PortfolioQuery>()
    val onUpdatePortfolioCommand = Action<UpdatePortfolioCommand>()
    val onCandidateStocksQuery = Action<CandidateStocksQuery>()
    val onPortfolioStockQuery = Action<PortfolioStockQuery>()

    fun run() {
        onPortfolioQuery(PortfolioQuery())
        menuLoop()
    }

    private fun menuLoop() {
        fun askUserForStockIdentification(onId: (String) -> Unit) {
            println("Identification?: ")
            val input = readLine()!!
            if (input == "") return
            onId(input)
        }

        loop@ while (true) {
            println(">>> (D)isplay, (B)uy, (S)ell, (U)pdate, E(x)it?: ")
            when (readLine()?.toUpperCase()) {
                "X" -> return
                "D" -> onPortfolioQuery(PortfolioQuery())
                "U" -> onUpdatePortfolioCommand(UpdatePortfolioCommand())
                "B" -> askUserForStockIdentification { onCandidateStocksQuery(CandidateStocksQuery(pattern = it)) }
                "S" -> askUserForStockIdentification { onPortfolioStockQuery(PortfolioStockQuery(pattern = it)) }
                null -> return
            }
        }
    }

    fun selectStockToBuy(candidates: CandidateStocksQueryResult): BuyStockCommand? {
        fun askUserForQtyToBuy(onQty: (Int) -> Unit) {
            print("Buy qty?: ")
            val input = readLine()!!
            if (input == "") return

            val qty = input.toInt()
            onQty(qty)
        }

        displayBuyCandidates(candidates.candidates)
        var command: BuyStockCommand? = null
        letUserSelectByIndex("Enter index of stock to buy: ") { index ->
            val chosenStock = candidates.candidates[index]
            displayChosenCandidate(chosenStock)
            askUserForQtyToBuy { qty ->
                command = BuyStockCommand(
                    stockName = chosenStock.name,
                    stockSymbol = chosenStock.symbol,
                    stockPriceCurrency = chosenStock.currency,
                    qty = qty,
                    stockPrice = chosenStock.price,
                    bought = LocalDate.now()
                )
            }
        }

        return command
    }

    fun selectStockToSell(candidates: PortfolioStockQueryResult): SellStockCommand? {
        var command: SellStockCommand? = null

        displaySellCandidates(candidates)
        letUserSelectByIndex("Enter index of stock to sell: ") { index ->
            val toSell = candidates.matchingStocks[index]
            command = SellStockCommand(stockSymbol = toSell.first)
        }

        return command
    }

    private fun letUserSelectByIndex(prompt: String, onIndex: (Int) -> Unit) {
        print(prompt)
        val input = readLine()!!
        if (input == "") return

        val index = input.toInt() - 1
        onIndex(index)
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

    fun displayBuyConfirmation(stockSymbol: String, qty: Int, price: Double) {
        println("Bought $qty x $stockSymbol at $price = ${qty * price}")
    }

    private fun displaySellCandidates(candidatesMatchingStocks: PortfolioStockQueryResult) {
        for (i in candidatesMatchingStocks.matchingStocks.indices) {
            println("${i + 1}. ${candidatesMatchingStocks.matchingStocks[i].first} (${candidatesMatchingStocks.matchingStocks[i].second})")
        }
    }

    fun display(portfolio: PortfolioQueryResult) {
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

    fun displaySellConfirmation(stockSymbol: String) {
        println("Sold all '${stockSymbol}'!")
    }
}
