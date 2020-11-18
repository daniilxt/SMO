class Buffer(var capacity: Int) {
    private val source = Source(true)
    private val queue = Array(capacity) { Application(source, -1,true) }
    private val cleanValue = Application(source, -1,true)
    private var index = 0
    private var space = 0

    fun add(app: Application) {
        if (space != capacity) {
            queue[index] = app
            index++
            space++
        }
    }

    fun pull(app: Application) {
        val appRemove = queue.maxBy { it.time }
        if (appRemove != null) {
            shiftQueue(appRemove)
            add(app)
        }
    }

    private fun shiftQueue(app: Application) {
        val indexDelete = queue.indexOf(app)

        for (i in indexDelete until queue.size - 1) {
            queue[i] = queue[i + 1]
        }
        space--
        index--
        queue[queue.lastIndex] = cleanValue
    }

    fun printQueue() {
        queue.forEach { println(it) }
    }

    fun getNext(): Application {
        val app = queue[0]
        shiftQueue(app)
        return app
    }

    fun isEmpty(): Boolean {
        if (space == 0) {
            return true
        }
        return false
    }

    fun isFull(): Boolean {
        return space == capacity
    }
}
/*
class Buffer(var capacity: Int) {
    private var index = -1
    private val queue = ArrayList<Application>(capacity)

    init {
        for (i in 0..capacity) {
            queue.add(Application(Source(), -1))
        }
    }

    fun add(app: Application) {
        if (queue.size < capacity) {
            queue.add(app)
            index++
        }
    }

    fun getNext(): Application {
        if (index == capacity) {
            index = 0
        }
        return queue[index]
    }

    fun isEmpty(): Boolean {
        return queue.isEmpty()
    }

    fun isFull(): Boolean {
        return queue.count() == capacity
    }

    fun pull(app: Application) {
        val appRemove = queue.maxBy { it.time }
        printQueue()
        queue.remove(appRemove)
        printQueue()
        for (i in 0 until queue.size) {
            queue[i] = queue[i + 1]
        }
        queue.add(app)
    }

    fun printQueue() {
        queue.forEach { println(it) }
    }
}*/
