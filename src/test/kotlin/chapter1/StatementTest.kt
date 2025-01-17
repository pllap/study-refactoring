package chapter1

import kotlin.test.Test
import kotlin.test.assertEquals

class StatementTest {
    @Test
    fun testStatement() {
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

        val expected = """
            청구 내역 (고객명: BigCo)
              Hamlet: $650.00 (55석)
              As You Like It: $580.00 (35석)
              Othello: $500.00 (40석)
            총액: $1,730.00
            적립 포인트: 47점
        """.trimIndent() + '\n'

        val actual = statement(invoice, plays)

        assertEquals(expected, actual)
    }

    @Test
    fun testHtmlStatement() {
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

        val expected = """
            <h1>청구 내역 (고객명: BigCo)</h1>
            <table>
            <tr><th>연극</th><th>좌석 수</th><th>금액</th></tr><tr><td>Hamlet</td><td>55석</td><td>${'$'}650.00</td></tr>
            <tr><td>As You Like It</td><td>35석</td><td>${'$'}580.00</td></tr>
            <tr><td>Othello</td><td>40석</td><td>${'$'}500.00</td></tr>
            </table>
            <p>총액: <em>${'$'}1,730.00</em></p>
            <p>적립 포인트: <em>47</em>점</p>
        """.trimIndent() + '\n'

        val actual = htmlStatement(invoice, plays)

        assertEquals(expected, actual)
    }
}
