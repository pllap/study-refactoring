package chapter1

import java.text.NumberFormat
import java.util.*

fun main() {
    val invoice = Invoice(
        "BigCo",
        listOf(
            Invoice.Performance("hamlet", 55),
            Invoice.Performance("as-like", 35),
            Invoice.Performance("othello", 40),
        )
    )

    val plays = mapOf(
        "hamlet" to Play("Hamlet", "tragedy"),
        "as-like" to Play("As You Like It", "comedy"),
        "othello" to Play("Othello", "tragedy"),
    )

    println(statement(invoice, plays))
}

fun statement(invoice: Invoice, plays: Map<String, Play>): String {
    var totalAmount = 0
    var volumeCredits = 0
    var result = "청구 내역 (고객명: ${invoice.customer})\n"
    val format = NumberFormat.getCurrencyInstance(Locale.US).also {
        it.maximumFractionDigits = 2
        it.minimumFractionDigits = 2
    }

    for (perf in invoice.performances) {
        val play = plays[perf.playID]!!
        var thisAmount: Int

        when (play.type) {
            "tragedy" -> {
                thisAmount = 40000
                if (perf.audience > 30) {
                    thisAmount += 1000 * (perf.audience - 30)
                }
            }
            "comedy" -> {
                thisAmount = 30000
                if (perf.audience > 20) {
                    thisAmount += 10000 + 500 * (perf.audience - 20)
                }
                thisAmount += 300 * perf.audience
            }
            else -> throw RuntimeException("알 수 없는 장르: ${play.type}")
        }

        // 포인트를 적립한다.
        volumeCredits += maxOf(perf.audience - 30, 0)
        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if ("comedy" == play.type) {
            volumeCredits += perf.audience / 5
        }

        // 청구 내역을 출력한다.
        result += "  ${play.name}: ${format.format(thisAmount / 100.0)} (${perf.audience}석)\n"
        totalAmount += thisAmount
    }

    result += "총액: ${format.format(totalAmount / 100.0)}\n"
    result += "적립 포인트: $volumeCredits 점\n"
    return result
}
