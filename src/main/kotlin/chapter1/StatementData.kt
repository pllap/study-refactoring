package chapter1

data class StatementData(
    val customer: String,
    val performances: List<Performance>,
) {
    data class Performance(
        val play: Play,
        val audience: Int,
    )
}
