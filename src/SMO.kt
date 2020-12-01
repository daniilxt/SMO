import java.io.BufferedWriter
import java.io.File
import kotlin.random.Random

class SMO(bufferCapacity: Int, sources: Int, devices: Int, lambda: Double) {
    private val buffer = Buffer(bufferCapacity)
    private val sources = List(sources) { it -> Source(lambda, Random.nextDouble()) }
    private val devices = List(devices) { it -> Device(lambda) }

    private val calendar = ApplicationCalendar
    var event = Event.SOURCE //bms1
    var time = 0L
    private var pointerDevice = -1

    //var pointerDevice = 0;

    fun runSMO() {
        val fileName = "C:\\Users\\Daniil\\IdeaProjects\\SMO\\src\\test.txt"
        val fileWrite = File(fileName).bufferedWriter()
        printStat()

        while (true) {
            devices.forEach {
                if (it.getEndTime() == time) {
                    it.isFree = true
                    val app = it.clear()
                    if (app != null) {
                        calendar.remove(app to Event.DEVICE)
                    }
                }
            }
            printStat()
            printCalendar(fileWrite)

            when (event) {

                Event.SOURCE -> {
                    sources.forEach { it ->
                        if (time >= it.getEndTime()) {
                            calendar.addApplication(it.generateApplication(time), Event.SOURCE)
                        }
                    }
                    // get min application, then get time
                    time = calendar.getMinTime(Event.SOURCE, Event.DEVICE)?.first?.time ?: time
                    event = Event.DEVICE
                }

                Event.BUFFER -> {
                    //buffer.printQueue()
                    val app = calendar.changeMinStatus(Event.SOURCE, Event.BUFFER)
                    time = calendar.getMinTime(Event.SOURCE, Event.DEVICE)?.first?.time ?: time
                    if (!buffer.isFull()) {
                        if (app != null) {
                            buffer.add(app)
                        }
                    } else {
                        if (app != null) {
                            val appTmp = buffer.getLast()
                            calendar.remove(appTmp to Event.BUFFER)
                            buffer.pull(app)
                        }
                    }
                    event = Event.SOURCE
                }
                Event.DEVICE -> {

                    event = Event.SOURCE
                    time = calendar.getMinTime(Event.SOURCE, Event.DEVICE)?.first?.time ?: time
                    // val freeDevice: Int? = devices.find { time >= it.getEndTime() }?.getNumber()
                    val freeDevice: Int? = findDevice()
                    if (freeDevice != null) {
                        var flag = false
                        var app: Application? = when (buffer.isEmpty()) {
                            true -> {
                                flag = true
                                calendar.getMinTime()?.first
                                //calendar.findMinTime()?.first
                            }
                            false -> {
                                println("BEFORE BUFFER___ ")
                                buffer.printQueue()
                                val tmpApp = buffer.getNext()
                                calendar.remove(tmpApp to Event.BUFFER)
                                tmpApp //return
                            }
                        }
                        if (app != null && app.time <= time) {
                            if (flag) {
                                app = calendar.findMinTime()?.first
                            }
                            devices[freeDevice].handle(app!!, time)
                            devices[freeDevice].isFree = false
                            app.time = devices[freeDevice].getEndTime()
                            calendar.addApplication(app, Event.DEVICE)

                        } else if (app != null) {
                            // если нет заявок, а прибор выбран
                            pointerDevice--
                        }

                    } else {
                        event = Event.BUFFER
                        println("Handlers is fulled")
                    }
                    devices.forEach { it ->
                        println("Device number ${it.getNumber()} status ${it.isFree} handled applications ${it.countApplications()} ")
                    }
                }
            }
            val read = readLine()
        }
    }

    private fun printCalendar(fileWrite: BufferedWriter) {
        fileWrite.write("--START--\n")
        fileWrite.write("Time is $time\n")
        calendar.getPrintApplications(fileWrite)
        fileWrite.write("--END--\n")
        fileWrite.flush()
    }

    private fun printStat() {

        println("Time is $time  event is: $event")
        println("--STAT--")
        sources.forEach { it ->
            println("Source number ${it.getNumber()} status ${it.countApplications()} TIME END: ${it.getEndTime()} ")
        }
        devices.forEach { it ->
            println("Device number ${it.getNumber()} status ${it.isFree} handled applications ${it.countApplications()} TIME END: ${it.getEndTime()} ")
        }
        buffer.printQueue()
        sources.forEach { it ->
            println("Source number ${it.getNumber()} status ${it.isFree()} created applications ${it.countApplications()} ")
        }
        println("--END STAT-- \n")
    }

    private fun findDevice(): Int? {
        var deviceIndex: Int? = null
        for (i in devices.indices) {
            pointerDevice++
            if (pointerDevice >= devices.size) {
                pointerDevice = 0
            }
            if (devices[pointerDevice].isFree && time >= devices[pointerDevice].getEndTime()) {
                deviceIndex = pointerDevice
                println("device is${devices[pointerDevice].getNumber()}  index is:${i}  ind is: $pointerDevice")
                break;
            }
        }
        return deviceIndex
    }
}