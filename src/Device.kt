import kotlin.math.ln
import kotlin.random.Random
import kotlin.random.Random.Default.nextDouble

class Device {
    init {
        number++
    }

    companion object {
        var number = 0
    }

    private var deviceNumber = number
    private var timeEnd = -1L
    private var status = true

    fun getNumber():Int {
        return deviceNumber
    }
    fun handle(app: Application) {
        var lambda = 3
        val x = (-1 / (3 * ln(Random.nextDouble()))*1000).toLong()
        timeEnd = app.time + x
        println("Handle application ${app.getNumber()} ${app.time}  time end = $timeEnd")
    }

    fun getEndTime(): Long {
        return timeEnd
    }

    fun isFree(): Boolean {
        return status
    }
}