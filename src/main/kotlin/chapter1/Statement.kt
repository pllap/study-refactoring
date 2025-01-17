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

    val statementData = StatementData(
        customer = invoice.customer,
        performances = invoice.performances.map {
            StatementData.Performance(
                play = playFor(it),
                audience = it.audience,
                amount = amountFor(it),
            )
        }
    )
    return renderPlainText(statementData)
}

fun renderPlainText(statementData: StatementData): String {
    fun volumeCreditsFor(performance: StatementData.Performance): Int {
        var result = 0
        // 포인트를 적립한다.
        result += maxOf(performance.audience - 30, 0)
        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if ("comedy" == performance.play.type) {
            result += performance.audience / 5
        }
        return result
    }

    fun totalVolumeCredits(): Int {
        var result = 0

        for (performance in statementData.performances) {
            result += volumeCreditsFor(performance)
        }

        return result
    }

    fun totalAmount(): Int {
        var result = 0

        for (performance in statementData.performances) {
            result += performance.amount
        }

        return result
    }

    fun usd(number: Int): String {
        return NumberFormat.getCurrencyInstance(Locale.US)
            .also {
                it.maximumFractionDigits = 2
                it.minimumFractionDigits = 2
            }
            .format(number / 100.0)
    }

    var result = "청구 내역 (고객명: ${statementData.customer})\n"

    for (performance in statementData.performances) {
        // 청구 내역을 출력한다.
        result += "  ${performance.play.name}: ${usd(performance.amount)} (${performance.audience}석)\n"
    }

    result += "총액: ${usd(totalAmount())}\n"
    result += "적립 포인트: ${totalVolumeCredits()}점\n"
    return result
}
