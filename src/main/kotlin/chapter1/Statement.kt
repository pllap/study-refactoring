package chapter1

import java.text.NumberFormat
import java.util.*

fun statement(invoice: Invoice, plays: Map<String, Play>): String {
    return renderPlainText(createStatementData(invoice, plays))
}

fun renderPlainText(statementData: StatementData): String {
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

    result += "총액: ${usd(statementData.totalAmount)}\n"
    result += "적립 포인트: ${statementData.totalVolumeCredits}점\n"
    return result
}
