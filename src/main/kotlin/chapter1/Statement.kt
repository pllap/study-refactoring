package chapter1

import java.text.NumberFormat
import java.util.*

fun statement(invoice: Invoice, plays: Map<String, Play>): String {
    fun playFor(performance: Invoice.Performance): Play {
        return plays[performance.playID] ?: throw RuntimeException("알 수 없는 장르: ${performance.playID}")
    }

    fun amountFor(performance: Invoice.Performance): Int {
        var result: Int

        when (playFor(performance).type) {
            "tragedy" -> {
                result = 40000
                if (performance.audience > 30) {
                    result += 1000 * (performance.audience - 30)
                }
            }

            "comedy" -> {
                result = 30000
                if (performance.audience > 20) {
                    result += 10000 + 500 * (performance.audience - 20)
                }
                result += 300 * performance.audience
            }

            else -> throw RuntimeException("알 수 없는 장르: ${playFor(performance).type}")
        }

        return result
    }

    var totalAmount = 0
    var volumeCredits = 0
    var result = "청구 내역 (고객명: ${invoice.customer})\n"
    val format = NumberFormat.getCurrencyInstance(Locale.US).also {
        it.maximumFractionDigits = 2
        it.minimumFractionDigits = 2
    }

    for (performance in invoice.performances) {
        val thisAmount = amountFor(performance)

        // 포인트를 적립한다.
        volumeCredits += maxOf(performance.audience - 30, 0)
        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if ("comedy" == playFor(performance).type) {
            volumeCredits += performance.audience / 5
        }

        // 청구 내역을 출력한다.
        result += "  ${playFor(performance).name}: ${format.format(thisAmount / 100.0)} (${performance.audience}석)\n"
        totalAmount += thisAmount
    }

    result += "총액: ${format.format(totalAmount / 100.0)}\n"
    result += "적립 포인트: ${volumeCredits}점\n"
    return result
}
