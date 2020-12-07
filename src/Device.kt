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
    private var countApplications = -1L
    private var timeEnd = -1L
    private var currentApp: Application? = null
    var isFree = true


    fun getNumber(): Int {
        return deviceNumber
    }

    fun handle(app: Application, time: Long) {
        isFree = false
        currentApp = app
        val x = ((-1 / (lambda * ln(Random.nextDouble() + 0.001))) * 100).toLong()
        timeEnd = time + x
        println("Handle application ${app.getNumber()} ${app.time}  time end = $timeEnd   x: $x")
        countApplications++
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
}