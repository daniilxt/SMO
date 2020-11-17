fun main() {

    val sources = List(3) { it -> Source() }
    val devices = List(2) { it -> Device() }

    val buffer = Buffer(5)
    val calendar = ApplicationCalendar
    var event = Event.SOURCE //bms1
    var time = 2L
    var minTime = 0L

    sources.forEach { it ->
        if (time >= it.getEndTime()) {
            calendar.addApplication(it.generateApplication(time), Event.SOURCE)
        }
    }
    calendar.addApplication(sources[1].generateApplication(time - 2), Event.BUFFER)
    calendar.findMinTime()?.first?.let {
        it.apply {
            devices[0].handle(this)
            this.time = devices[0].getEndTime() // rewrite application time
        }
        calendar.addApplication(it, Event.DEVICE)
    }
    calendar.printApplications()
    println(calendar.getMinTime(Event.SOURCE, Event.DEVICE))
    println(calendar.getMinTime())
/*
    while (true) {
        println("Time is $time")
        devices.forEach{ it ->
            println("Device number ${it.getNumber()} status ${it.isFree} handled applications ${it.countApplications()}")
        }
        calendar.printApplications()
        minTime = calendar.findMinTime()?.first?.time ?: minTime
        when (event) {
            Event.BUFFER -> {
                if (!buffer.isFull()) {
                    calendar.changeMinStatus(Event.SOURCE, Event.BUFFER)?.let { buffer.add(it) }
                }
            }
            Event.DEVICE -> {
                val freeDevice: Int? = devices.find { time > it.getEndTime() }?.getNumber()
                if (freeDevice != null) {
                    devices[freeDevice-1].isFree = false
                    calendar.findMinTime()?.first?.let { devices[freeDevice-1].handle(it) }
                } else {
                    Event.BUFFER
                    println("Handle is worked")
                }
                devices.forEach{ it ->
                    println("Device number ${it.getNumber()} status ${it.isFree} handled applications ${it.countApplications()} ")

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

    }*/

}