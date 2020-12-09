package Service

import java.lang.Exception

class Stat(src: Int) {

    private val devHandledSources = Array(src) { it -> 0 }
    private val devDeniedSources = Array(src) { it -> 0 }

    fun devHandled(source: Int) {
        devHandledSources[source - 1]++
    }

    fun devDenied(source: Int) {
        devDeniedSources[source - 1]++
    }

    fun printStat() {
        devDeniedSources.forEachIndexed { index, i ->
            try {
                println("Source: ${index + 1}  count den $i count hand ${devHandledSources[index]}  count Potk: ${(i.toDouble() / devHandledSources[index])} ")
            } catch (ex: Exception) {
                println("by zero")
            }
        }
    }
}