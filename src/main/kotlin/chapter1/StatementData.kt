package chapter1

data class StatementData(
    val customer: String,
    val performances: List<Performance>,
    val totalAmount: Int,
    val totalVolumeCredits: Int,
) {
    data class Performance(
        val play: Play,
        val audience: Int,
        val amount: Int,
        val volumeCredits: Int,
    )
}
