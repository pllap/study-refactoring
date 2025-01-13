package chapter1

data class Invoice(
    val customer: String,
    val performances: List<Performance>,
) {
    data class Performance(
        val playID: String,
        val audience: Int,
    )
}
