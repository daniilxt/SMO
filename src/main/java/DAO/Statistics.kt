package DAO

data class Statistics(
    val src: Long,
    val countApp: Long,
    val pDenied: Double,
    val timeWait: Double,
    val timeHandle: Double,
    val timeSystem: Double,
    val dispWait: Double,
    val dispHandled: Double
)
