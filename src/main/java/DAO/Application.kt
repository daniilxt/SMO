package DAO

import Service.Source

data class Application(var source: Source, var time: Long, var isSystem:Boolean = false) {
    init {
        number += 1
        if (isSystem){
            number = 0
        }
    }
    val applicationNumber = number
    var event = Event.SOURCE
    var src = source.getNumber()

    fun getNumber(): Long {
        return applicationNumber
    }

    companion object {
        var number = 0L
    }
}