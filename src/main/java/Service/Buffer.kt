package Service

import DAO.Application
import java.util.*
import kotlin.random.Random

class Buffer(private val capacity: Int) {
    private val source = Source(1.3, Random.nextDouble(), true)
    private val queue = Array(capacity) { Application(source, -1, true) }
    private val cleanValue = Application(source, -1, true)
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
        // val appRemove = queue.maxBy { it.time }
        val appRemove = getLast()
        shiftQueue(appRemove)
        add(app)
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
        queue.forEach { println("App num: ${it.getNumber()} time=${it.time}, source:${it.source.getNumber()} ") }
    }

    fun getNext(): Application {
        val app = queue.minWith(Comparator { p1, p2 ->
            when {
                //источник выше приоритетом
                (p1.source.getNumber() != 0 && p2.source.getNumber() != 0) && (p1.source.getNumber() > p2.source.getNumber()) -> 1
                // источники одинаковые, выбираем мин время
                (p1.source.getNumber() != 0 && p2.source.getNumber() != 0) && (p1.source.getNumber() == p2.source.getNumber()) && (p1.time > p2.time) -> 1
                //одинаковые
                (p1.source.getNumber() != 0 && p2.source.getNumber() != 0) && (p1.source.getNumber() == p2.source.getNumber()) && (p1.time == p2.time) -> 0
                else -> -1
            }
        })
        // val app = queue[0]
        if (app != null) {
            shiftQueue(app)
        }
        return app!!
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

    fun getLast(): Application {
        return queue[capacity - 1]
    }

    fun getList(): MutableList<Application> {
        return queue.filter { !it.isSystem }.toMutableList()
    }
}
