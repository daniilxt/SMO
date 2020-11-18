import kotlin.math.ln
import kotlin.random.Random

class Device {
    init {
        number++
    }

    companion object {
        var number = 0
    }

    private var deviceNumber = number
    private var countApplications = 0L
    private var timeEnd = -1L
    var isFree = true

    fun getNumber(): Int {
        return deviceNumber
    }

    fun handle(app: Application) {
        val lambda = 1.3
        isFree = false
       // val x = (-1 / (1.4 * ln(Random.nextDouble())) * 1000).toLong()
        val x = (-1 / (lambda * ln(1- Random.nextDouble())) * 100).toLong()
        timeEnd = app.time + x
        println("Handle application ${app.getNumber()} ${app.time}  time end = $timeEnd   x: $x")
        countApplications++
    }

    fun getEndTime(): Long {
        return timeEnd
    }

    fun countApplications(): Long {
        return countApplications
    }
}