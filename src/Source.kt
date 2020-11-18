import kotlin.math.ln
import kotlin.random.Random

class Source(var isSystem:Boolean = false) {
    init {
        number += 1
        if (isSystem){
            number = 0
        }
    }

    private val sourceNumber = number

    // private val generatedStrategy = abs(2 * log10(Math.random()) * 1000).toLong()
    private var lambda = 1.3

    // private val generatedStrategy = (-1 / (1.2 * ln(Random.nextDouble())) * 1000).toLong()
    private val generatedStrategy = (-1 / (lambda * ln(1 - Random.nextDouble())) * 100).toLong()

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