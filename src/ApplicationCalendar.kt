object ApplicationCalendar {
    private val applicationList = mutableListOf<Pair<Application,Event>>()

    fun addApplication(app:Application,event:Event) {
        applicationList.add((app) to event)
    }
    fun printApplications(){
        applicationList.forEach {println("${it.first.source.getNumber()} ${it.first.time}  ${it.second}")}
    }

    fun findMinTime(): Pair<Application, Event>? {
        val app = applicationList.minBy { it.first.time}
        applicationList.remove(app)

        if (app != null) {
            println("Applist app is ${app.first.getNumber()}")
        }
        return app
    }
    fun getMinTime(): Pair<Application, Event>? {
        return applicationList.minBy { it.first.time}
    }

}