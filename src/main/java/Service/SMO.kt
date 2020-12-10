package Service

import DAO.Application
import DAO.Event
import DAO.Statistics
import java.io.BufferedWriter
import java.io.File
import kotlin.random.Random

class SMO(
    bufferCapacity: Int,
    sources: Int,
    devices: Int,
    var appCount: Long,
    lambda: Double,
    var isStepMode: Boolean = false
) {
    private val buffer = Buffer(bufferCapacity)
    private val sources = List(sources) { it -> Source(lambda, Random.nextDouble()) }
    private val devices = List(devices) { it -> Device(lambda) }

    private val calendar = ApplicationCalendar
    var event = Event.DEVICE //bms1
    var time = 0L
    private var pointerDevice = -1
    private var nextStep = false

    val fileName = "C:\\Users\\Daniil\\IdeaProjects\\SMO\\src\\main\\java\\test.txt"
    val fileWrite = File(fileName).bufferedWriter()
    var flagEnd = 0L
    var stat = Stat(sources)


    fun runSMO() {
        while ((appCount >= flagEnd)) {
            runStepSMO() {}
        }
        stat.printStat(sources)
        calendar.printAppsAfter(time)

    }

    fun runStepSMO(callback: () -> Unit) {
        devices.forEach {
            if (it.getEndTime() == time) {
                it.isFree = true
                val app = it.clear()
                if (app != null) {
                    calendar.remove(app to Event.DEVICE)
                }
            }
        }
        //printStat()
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
                time = calendar.getMinTime(Event.SOURCE, Event.DEVICE)?.first?.time ?: time
                val app = calendar.changeMinStatus(Event.SOURCE, Event.BUFFER)
                if (!buffer.isFull()) {
                    if (app != null) {
                        buffer.add(app)
                    }
                } else {
                    if (app != null) {
                        val appTmp = buffer.getLast()
                        println("DENY is ${appTmp.src}")
                        stat.devDenied(appTmp.src)
                        calendar.remove(appTmp to Event.BUFFER)
                        buffer.pull(app)
                    }
                }
                event = Event.SOURCE
            }
            Event.DEVICE -> {

                event = Event.SOURCE
                time = calendar.getMinTime(Event.SOURCE, Event.DEVICE)?.first?.time ?: time
                val freeDevice: Int? = findDevice()
                if (freeDevice != null) {
                    var flag = false
                    var app: Application? = when (buffer.isEmpty()) {
                        true -> {
                            flag = true
                            calendar.getMinTime()?.first
                        }
                        false -> {
                            println("BEFORE BUFFER___ ")
                            buffer.printQueue()
                            val tmpApp = buffer.getNext()
                            stat.timeInBuffer(tmpApp.src, time - tmpApp.time)
                            stat.countInBuffer(tmpApp.src)
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
                        println("ALLOW is ${app.src}")
                        stat.devHandled(app.src)
                        app.time = devices[freeDevice].getEndTime()
                        stat.devTimeHandled(app.src, devices[freeDevice].handledTime())
                        calendar.addApplication(app, Event.DEVICE)

                    } else if (app != null) {
                        // если нет заявок, а прибор выбран
                        pointerDevice--
                    } else {
                        pointerDevice--
                    }

                } else {
                    event = Event.BUFFER
                    println("Handlers is fulled")
                }
                devices.forEach { it ->
                    println("Service.Device number ${it.getNumber()} status ${it.isFree} handled applications ${it.countApplications()} ")
                }
            }
        }
        flagEnd = checkAppCount()

        callback()

    }

    private fun allDevicesFree(): Boolean {
        return devices.stream().allMatch { it.isFree && it.countApplications() > 0 }
    }

    private fun checkAppCount(): Long {
        return devices.map { it.countApplications() }.sum()
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
            println("Service.Source number ${it.getNumber()} TIME END: ${it.getEndTime()} ")
        }
        devices.forEach { it ->
            println("Service.Device number ${it.getNumber()} status ${it.isFree} handled applications ${it.countApplications()} TIME END: ${it.getEndTime()} ")
        }
        buffer.printQueue()
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
                break
            }
        }
        return deviceIndex
    }

    // for fx
    fun getApps(): MutableList<Application> {
        return calendar.getAppList()
    }

    fun getBuffer(): MutableList<Application> {
        return buffer.getList()
    }

    fun getCurrentTime(): Long {
        return time
    }

    fun getStatistics(): MutableList<Statistics> {
        return stat.getStatistics(sources)
    }
}