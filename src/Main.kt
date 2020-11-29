import java.io.File

fun main() {
    val buffer = Buffer(3)
    val sources = List(3) { it -> Source() }
    val devices = List(2) { it -> Device(1.3) }

    val calendar = ApplicationCalendar
    var event = Event.SOURCE //bms1
    var time = 0L


/*    sources.forEach { it ->
        if (time >= it.getEndTime()) {
            //calendar.addApplication(it.generateApplication(time), Event.SOURCE)
            buffer.add(it.generateApplication(time))
        }
    }*/
/*
    val test = sources[0].generateApplication(time+ 1488)
    val test2 = sources[0].generateApplication(time+ 20)
    val test3 = sources[0].generateApplication(time+ 120)
    buffer.add(test2)
    buffer.add(test3)

    buffer.printQueue()
    println("After ______________________")
    buffer.pull(test)
    buffer.printQueue()
    println("After NEXT ______________________")
    buffer.getNext()
    buffer.printQueue()
*/

    val fileName = "C:\\Users\\Daniil\\IdeaProjects\\SMO\\src\\test.txt"
    val fileWrite = File(fileName).bufferedWriter()

    while (true) {

        println("Time is $time")
        //  fileWrite.write("Time is $time\n  ${calendar.getPrintApplications()}")
        fileWrite.write("Time is $time\n")
        calendar.getPrintApplications(fileWrite)
        fileWrite.flush()

        println("__CALENDAR__")
        calendar.printApplications()
        println("__END__")
        when (event) {

            Event.SOURCE -> {
               // println("BEFORE SOURCE ")
               // calendar.printApplications()
                sources.forEach { it ->
                    if (time >= it.getEndTime()) {
                        calendar.addApplication(it.generateApplication(time), Event.SOURCE)
                    }
                }
                // get min application, then get time
               time = calendar.getMinTime(Event.SOURCE, Event.DEVICE)?.first?.time ?: time
                event = Event.DEVICE
                //println("AFTER SOURCE ")
               // calendar.printApplications()
            }

            Event.BUFFER -> {
                println(" BUFFER--- ")
                buffer.printQueue()
                val app = calendar.changeMinStatus(Event.SOURCE, Event.BUFFER)
                if (!buffer.isFull()) {
                    if (app != null) {
                        buffer.add(app)
                    }
                } else {
                    if (app != null) {
                        calendar.remove(app to Event.BUFFER)
                        buffer.pull(app)
                    }
                }
                event = Event.SOURCE
              /*   println("AFTER BUFFER ")
                calendar.printApplications()*/
            }
            Event.DEVICE -> {
                //println("BEFORE DEVICE___ ")
                //calendar.printApplications()
                event = Event.SOURCE
                time = calendar.getMinTime(Event.SOURCE, Event.DEVICE)?.first?.time ?: time

                val freeDevice: Int? = devices.find { time >= it.getEndTime() }?.getNumber()
                if (freeDevice != null) {
                    val app: Application? = when (buffer.isEmpty()) {
                        true -> {
                            calendar.findMinTime()?.first
                        }
                        false -> {
                            println("BEFORE BUFFER___ ")
                            buffer.printQueue()
                            val tmpApp = buffer.getNext()
                            calendar.remove(tmpApp to Event.BUFFER)
                            tmpApp //return
                        }
                    }
                    if (app != null) {
                        devices[freeDevice - 1].handle(app,time)
                        devices[freeDevice - 1].isFree = false
                        app.time = devices[freeDevice-1].getEndTime()
                        calendar.addApplication(app, Event.DEVICE)

                    }

                } else {
                    event = Event.BUFFER
                    println("Handlers is fulled")
                }
                devices.forEach { it ->
                    // println("Device number ${it.getNumber()} status ${it.isFree} handled applications ${it.countApplications()} ")
                    println("Device number ${it.getNumber()} status ${it.isFree} handled applications ${it.countApplications()} ")
                }
               // println("AFTER DEVICE___ ")
                //calendar.printApplications()
            }
        }
        val read = readLine()


    }
}