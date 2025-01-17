package chapter1

open class PerformanceCalculator(
    val performance: Invoice.Performance,
    val play: Play,
) {
    open val amount: Int
        get() {
            var result: Int

            when (this.play.type) {
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

                else -> throw RuntimeException("알 수 없는 장르: ${this.play.type}")
            }

            return result
        }

    open val volumeCredits: Int
        get() {
            var result = 0

            // 포인트를 적립한다.
            result += maxOf(performance.audience - 30, 0)
            // 희극 관객 5명마다 추가 포인트를 제공한다.
            if ("comedy" == this.play.type) {
                result += performance.audience / 5
            }

            return result
        }
}

class TragedyCalculator(
    performance: Invoice.Performance,
    play: Play,
) : PerformanceCalculator(performance = performance, play = play) {
    override val amount: Int
        get() {
            var result = 40000

            if (performance.audience > 30) {
                result += 1000 * (performance.audience - 30)
            }

            return result
        }

    override val volumeCredits: Int
        get() {
            return maxOf(performance.audience - 30, 0)
        }
}

class ComedyCalculator(
    performance: Invoice.Performance,
    play: Play,
) : PerformanceCalculator(performance = performance, play = play) {
    override val amount: Int
        get() {
            var result = 30000

            if (performance.audience > 20) {
                result += 10000 + 500 * (performance.audience - 20)
            }

            result += 300 * performance.audience

            return result
        }

    override val volumeCredits: Int
        get() {
            var result = 0

            result += maxOf(performance.audience - 30, 0)
            result += performance.audience / 5

            return result
        }
}

fun createPerformanceCalculator(performance: Invoice.Performance, play: Play): PerformanceCalculator {
    when (play.name) {
        "tragedy" -> return TragedyCalculator(performance, play)
        "comedy" -> return ComedyCalculator(performance, play)
    }
    return PerformanceCalculator(performance, play)
}

fun createStatementData(invoice: Invoice, plays: Map<String, Play>): StatementData {
    fun playFor(performance: Invoice.Performance): Play {
        return plays[performance.playID] ?: throw RuntimeException("알 수 없는 장르: ${performance.playID}")
    }

    fun totalAmount(performances: List<Invoice.Performance>): Int {
        return performances
            .map { PerformanceCalculator(it, playFor(it)).amount }
            .reduce { total, amount -> total + amount }
    }

    fun totalVolumeCredits(performances: List<Invoice.Performance>): Int {
        return performances
            .map { PerformanceCalculator(it, playFor(it)).volumeCredits }
            .reduce { total, volumeCredits -> total + volumeCredits }
    }

    return StatementData(
        customer = invoice.customer,
        performances = invoice.performances.map {
            val calculator = PerformanceCalculator(it, playFor(it))

            StatementData.Performance(
                play = calculator.play,
                audience = calculator.performance.audience,
                amount = calculator.amount,
                volumeCredits = calculator.volumeCredits,
            )
        },
        totalAmount = totalAmount(invoice.performances),
        totalVolumeCredits = totalVolumeCredits(invoice.performances),
    )
}
