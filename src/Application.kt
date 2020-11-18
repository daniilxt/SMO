data class Application(var source: Source, var time: Long,var isSystem:Boolean = false) {
    init {
        number += 1
        if (isSystem){
            number = 0
        }
    }
    private val applicationNumber = number
    fun getNumber(): Long {
        return applicationNumber
    }

    companion object {
        var number = 0L
    }
}