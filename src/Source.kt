import kotlin.math.exp
import kotlin.math.ln
import kotlin.random.Random

class Source(private val lambda: Double, private val tau: Double, isSystem: Boolean = false) {
    init {
        number += 1
        if (isSystem) {
            number = 0
        }
    }

    private val sourceNumber = number

    // private val generatedStrategy = (-1 / (lambda * ln(1 - Random.nextDouble())) * 100).toLong()
    private var timeEnd = -1L
    private var status = true
    private var countApplications = 0L

    fun getNumber(): Int {
        return sourceNumber
    }


    fun generateApplication(time: Long): Application {
        //time + равномерное распределение
        timeEnd = time + (lambda + tau * 1000).toLong()
        //timeEnd = time + ((1 - exp(-lambda * Random.nextDouble())) * 1000).toLong()
        val app = Application(this, timeEnd)
        println("Application ${app.getNumber()} from Source $sourceNumber will generated in time ${app.time}")
        countApplications++
        return app
    }

    fun getEndTime(): Long {
        return timeEnd
    }

    fun isFree(): Boolean {
        return status
    }

    fun countApplications(): Long {
        return countApplications
    }

    companion object {
        private var number = 0
    }
}