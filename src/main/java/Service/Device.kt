package Service

import DAO.Application
import kotlin.math.abs
import kotlin.math.ln
import kotlin.random.Random

class Device(private val lambda: Double) {
    init {
        number++
    }

    companion object {
        var number = 0
    }

    private var deviceNumber = number
    private var countApplications = 0L
    private var timeEnd = -1L
    private var currentApp: Application? = null
    private var handledTime: Long = 0L
    var isFree = true


    fun getNumber(): Int {
        return deviceNumber
    }

    fun handle(app: Application, time: Long) {
        isFree = false
        currentApp = app
        val x = abs(((-1 / (lambda * ln(Random.nextDouble() + 0.001))) * 500)).toLong()
        handledTime = x
        timeEnd = time + x
        countApplications++
        println("Handle application ${app.getNumber()} ${app.time}  time end = $timeEnd   x: $x")
    }

    fun getEndTime(): Long {
        return timeEnd
    }

    fun countApplications(): Long {
        return countApplications
    }

    fun clear(): Application? {
        timeEnd = 0
        return currentApp
    }

    fun handledTime(): Long {
        return handledTime
    }
}