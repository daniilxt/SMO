import java.util.*
import kotlin.math.ln
import kotlin.random.Random

fun main() {

    val sources = List(3) { it -> Source() }
    val devices = List(2) { it -> Device() }

    val buffer = Buffer(5)
/*    val Max = 20
    val Min = 3
    println((Math.random() * (Max - Min)) + Min)
    println((Math.random() * (Max - Min)) + Min)
    println((Math.random() * (Max - Min)) + Min)
    println((Math.random() * (Max - Min)) + Min)
    val random = Math.random() * (Max - Min) + Min

    println(Min + (Math.random() * (Max - Min) + Min) * (Max - Min))*/
/*val calendar = ApplicationCalendar
    for (i in 0..2) {
        calendar.addApplication(sources[i].generateApplication(),Event.SOURCE)
    }
    calendar.printApplications()
    println(calendar.findMinTime())*/

    val calendar = ApplicationCalendar
    var event = Event.SOURCE //bms1
    var time = 0L

    while (true) {
        println("Time is $time")
        calendar.printApplications()
        when (event) {
            Event.BUFFER -> {
                if (!buffer.isFull()) {
                    sources.forEach { it -> it.generateApplication(time) }
                }
            }
            Event.DEVICE -> {
                val freeDevice: Int? = devices.find { time > it.getEndTime() }?.getNumber()
                if (freeDevice != null) {
                    calendar.findMinTime()?.first?.let { devices[freeDevice].handle(it) }
                }
                event = Event.SOURCE
            }
            Event.SOURCE -> {
                sources.forEach { it ->
                    if (time >= it.getEndTime()) {
                        calendar.addApplication(it.generateApplication(time), Event.SOURCE)
                    }
                    // get min application, then get time
                    time = calendar.getMinTime()?.first?.time ?: time
                    event = Event.DEVICE
                }
            }
        }
        val read = readLine()

    }
}