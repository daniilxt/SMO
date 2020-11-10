import kotlin.math.ln
import kotlin.random.Random

class Source {
    init {
        number += 1
    }

    private val sourceNumber = number

    // private val generatedStrategy = abs(2 * log10(Math.random()) * 1000).toLong()
    private var lambda = 3
    //private val generatedStrategy = (1000 * (-1 / (lambda * ln(nextDouble())))).toLong()
    private val generatedStrategy = (-1 / (3 * ln(Random.nextDouble()))*1000).toLong()
    private var timeEnd = -1L
    private var status = true

    fun getNumber(): Int {
        return sourceNumber
    }


    fun generateApplication(time: Long): Application {

        timeEnd = time + generatedStrategy
        val app = Application(this, timeEnd)
        println("Application ${app.getNumber()} from Source $sourceNumber will generated in time ${app.time}")
        return app
    }

    fun getEndTime(): Long {
        return timeEnd
    }

    fun isFree(): Boolean {
        return status
    }

    companion object {
        private var number = 0
    }
}