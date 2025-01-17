package chapter1

import java.text.NumberFormat
import java.util.*

fun statement(invoice: Invoice, plays: Map<String, Play>): String {
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

    return renderPlainText(createStatementData(invoice, plays))
}

fun htmlStatement(invoice: Invoice, plays: Map<String, Play>): String {
    fun renderHtml(statementData: StatementData): String {
        fun usd(number: Int): String {
            return NumberFormat.getCurrencyInstance(Locale.US)
                .also {
                    it.maximumFractionDigits = 2
                    it.minimumFractionDigits = 2
                }
                .format(number / 100.0)
        }

        var result = "<h1>청구 내역 (고객명: ${statementData.customer})</h1>\n"
        result += "<table>\n"
        result += "<tr><th>연극</th><th>좌석 수</th><th>금액</th></tr>"
        for (performance in statementData.performances) {
            result += "<tr><td>${performance.play.name}</td><td>${performance.audience}석</td><td>${usd(performance.amount)}</td></tr>\n"
        }
        result += "</table>\n"
        result += "<p>총액: <em>${usd(statementData.totalAmount)}</em></p>\n"
        result += "<p>적립 포인트: <em>${statementData.totalVolumeCredits}</em>점</p>\n"
        return result
    }

    return renderHtml(createStatementData(invoice, plays))
}
