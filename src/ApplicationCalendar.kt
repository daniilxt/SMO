import java.io.BufferedWriter

object ApplicationCalendar {
    private val applicationList = mutableListOf<Pair<Application, Event>>()

    fun addApplication(app: Application, event: Event) {
        applicationList.add((app) to event)
    }

    fun printApplications() {
        applicationList.forEach { println("${it.first.source.getNumber()} ${it.first.time}  ${it.second}") }
    }

    override fun toString(): String {
        return applicationList.toString()
    }

    fun findMinTime(event1: Event = Event.SOURCE, event2: Event = Event.SOURCE): Pair<Application, Event>? {
        // val app = applicationList.minBy { it.first.time }
        val app = getMinTime(event1, event2)
        applicationList.remove(app)
        return app
    }

    fun getMinTime(event1: Event = Event.SOURCE, event2: Event = Event.SOURCE): Pair<Application, Event>? {
        val eventList = listOf(event1, event2)
        return applicationList.minWith(Comparator { p1, p2 ->
            when {
                (p1.first.time > p2.first.time) && (p1.second in eventList && p2.second in eventList) -> 1
                (p1.first.time == p2.first.time) && (p1.second in eventList && p2.second in eventList) -> 0
                else -> -1
            }
        })
    }

    fun changeMinStatus(source: Event, buffer: Event): Application? {
        //val minApplication = applicationList.minBy { it.first.time }
        val minApplication = getMinTime(Event.SOURCE, Event.DEVICE)
        val index = applicationList.indexOf(minApplication)
        if (minApplication != null) {
            applicationList[index] = Pair(minApplication.first, Event.BUFFER)
        }
        return minApplication?.first
    }

    fun getPrintApplications(fileWrite: BufferedWriter) {
        if (applicationList.isNotEmpty()) {
            applicationList.forEach { it ->
                fileWrite.write("App num = ${it.first.getNumber()} App source ${it.first.source.getNumber()} App time ${it.first.time}  App type ${it.second} \n")
                fileWrite.flush()
            }
        }
    }

    fun remove(pair: Pair<Application, Event>) {
        applicationList.remove(pair)
    }
}