package chapter1

class PerformanceCalculator(
    val performance: Invoice.Performance,
)

fun createStatementData(invoice: Invoice, plays: Map<String, Play>): StatementData {
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

    fun volumeCreditsFor(performance: Invoice.Performance): Int {
        var result = 0
        // 포인트를 적립한다.
        result += maxOf(performance.audience - 30, 0)
        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if ("comedy" == playFor(performance).type) {
            result += performance.audience / 5
        }
        return result
    }

    fun totalAmount(performances: List<Invoice.Performance>): Int {
        return performances
            .map { amountFor(it) }
            .reduce { total, amount -> total + amount }
    }

    fun totalVolumeCredits(performances: List<Invoice.Performance>): Int {
        return performances
            .map { volumeCreditsFor(it) }
            .reduce { total, volumeCredits -> total + volumeCredits }
    }

    return StatementData(
        customer = invoice.customer,
        performances = invoice.performances.map {
            StatementData.Performance(
                play = playFor(it),
                audience = it.audience,
                amount = amountFor(it),
                volumeCredits = volumeCreditsFor(it),
            )
        },
        totalAmount = totalAmount(invoice.performances),
        totalVolumeCredits = totalVolumeCredits(invoice.performances),
    )
}
