data class Application(var source: Source, var time: Long) {
    init {
        number += 1
    }
    private val applicationNumber = number
    fun getNumber(): Long {
        return applicationNumber
    }

    companion object {
        var number = 0L
    }
}