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
}
