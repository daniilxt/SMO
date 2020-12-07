package Service

import DAO.Application
import DAO.Event
import java.io.BufferedWriter

object ApplicationCalendar {
    private val applicationList = mutableListOf<Pair<Application, Event>>()

    fun addApplication(app: Application, event: Event) {
        applicationList.add((app) to event)
    }

    override fun toString(): String {
        return applicationList.toString()
    }

    fun findMinTime(event1: Event = Event.SOURCE, event2: Event = Event.SOURCE): Pair<Application, Event>? {
        val app = getMinTime(event1, event2)
        if (app != null) {
            applicationList.remove(app)
        }
        return app
    }

    fun checkMinTime(event1: Event = Event.SOURCE, event2: Event = Event.SOURCE): Pair<Application, Event>? {
        return getMinTime(event1, event2)
    }

    fun getMinTime(event1: Event = Event.SOURCE, event2: Event = Event.SOURCE): Pair<Application, Event>? {
        val eventList = listOf(event1, event2)
        return applicationList.filter { it.second == event1 || it.second == event2 }.minBy { it -> it.first.time }
    }

    fun changeMinStatus(source: Event, buffer: Event): Application? {
        val minApplication = getMinTime(source, Event.DEVICE)
        val index = applicationList.indexOf(minApplication)
        if (minApplication != null && minApplication.second != buffer) {
            applicationList[index] = Pair(minApplication.first, buffer)
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