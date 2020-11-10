class Buffer(var capacity: Int) {
    private var index = -1
    private val queue = ArrayList<Application>(capacity)

    fun add(app: Application) {
        if (queue.size < capacity) {
            queue.add(app)
            index++
        }
    }

    fun getNext() {
        if (index == capacity) {
            index = 0
        }

        queue[index]
    }

    fun isEmpty(): Boolean {
        return queue.isEmpty()
    }

    fun isFull(): Boolean {
        return queue.count() == capacity
    }
}