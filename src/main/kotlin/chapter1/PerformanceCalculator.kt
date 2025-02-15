package chapter1

open class PerformanceCalculator(
    val performance: Invoice.Performance,
    val play: Play,
) {
    open val amount: Int
        get() {
            throw NotImplementedError("서브클래스에서 구현해야 함")
        }

    open val volumeCredits: Int
        get() {
            throw NotImplementedError("서브클래스에서 구현해야 함")
        }
}

class TragedyCalculator(
    performance: Invoice.Performance,
    play: Play,
) : PerformanceCalculator(performance = performance, play = play) {
    override val amount: Int
        get() {
            if (this.play.type != "tragedy") {
                throw RuntimeException("오류 발생")
            }

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
            if (this.play.type != "comedy") {
                throw RuntimeException("오류 발생")
            }

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
    return when (play.type) {
        "tragedy" -> TragedyCalculator(performance, play)
        "comedy" -> ComedyCalculator(performance, play)
        else -> throw RuntimeException("알 수 없는 장르: ${play.type}")
    }
}

fun createStatementData(invoice: Invoice, plays: Map<String, Play>): StatementData {
    fun playFor(performance: Invoice.Performance): Play {
        return plays[performance.playID] ?: throw RuntimeException("알 수 없는 장르: ${performance.playID}")
    }

    fun totalAmount(performances: List<Invoice.Performance>): Int {
        return performances
            .map { createPerformanceCalculator(it, playFor(it)).amount }
            .reduce { total, amount -> total + amount }
    }

    fun totalVolumeCredits(performances: List<Invoice.Performance>): Int {
        return performances
            .map { createPerformanceCalculator(it, playFor(it)).volumeCredits }
            .reduce { total, volumeCredits -> total + volumeCredits }
    }

    return StatementData(
        customer = invoice.customer,
        performances = invoice.performances.map {
            val calculator = createPerformanceCalculator(it, playFor(it))

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
