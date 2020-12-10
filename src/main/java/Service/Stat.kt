package Service

import DAO.Statistics

class Stat(val src: Int) {

    private val devHandledSources = Array(src) { it -> 0 }
    private val devDeniedSources = Array(src) { it -> 0 }
    private val devTimeHandledSources = Array(src) { it -> 0L }
    private val timeInBuffer = Array(src) { it -> 0L }
    private val countInBuff = Array(src) { it -> 0L }
    private val timeInSystem = Array(src) { it -> 0L }


    private val statObj = mutableListOf<Statistics>()

    fun devHandled(source: Int) {
        devHandledSources[source - 1]++
    }

    fun devDenied(source: Int) {
        devDeniedSources[source - 1]++
    }

    fun devTimeHandled(source: Int, time: Long) {
        devTimeHandledSources[source - 1] += time
    }

    fun timeInBuffer(source: Int, time: Long) {
        timeInBuffer[source - 1] += time
    }

    fun countInBuffer(source: Int) {
        countInBuff[source - 1]++
    }

    fun printStat(sources:List<Source>) {
        calculateStatistics(sources)
        statObj.forEach { println(it) }
/*
        devDeniedSources.forEachIndexed { index, i ->
            try {
                println("Source: ${index + 1}  count den $i count hand ${devHandledSources[index]}  count Potk: ${(i.toDouble() / devHandledSources[index])} ")
            } catch (ex: Exception) {
                println("by zero")
            }
        }
       devTimeHandledSources.forEachIndexed { index, i ->
            try {
                println("Source: ${index + 1}  timeSUM $i count hand ${devHandledSources[index]}  timeOBSLSR: ${(i.toDouble() / devHandledSources[index])} ")
            } catch (ex: Exception) {
                println("by zero")
            }
        }
        timeInBuffer.forEachIndexed { index, i ->
            try {
                println("Source: ${index + 1}  timeInBuff $i count in buff ${countInBuff[index]}  SRTimeBuff: ${(i.toDouble() / countInBuff[index])} ")
                timeInSystem[index] =
                    devTimeHandledSources[index] / devHandledSources[index] + timeInBuffer[index] / countInBuff[index]
            } catch (ex: Exception) {
                println("by zero")
            }
        }
        timeInSystem.forEachIndexed { index, i ->
            try {
                println("Source: ${index + 1}  timeInSYSM $i")
            } catch (ex: Exception) {
                println("by zero")
            }
        }
    }*/
    }

    fun getStatistics(sources: List<Source>): MutableList<Statistics> {
        calculateStatistics(sources)
        return statObj
    }

    private fun calculateStatistics(sources: List<Source>) {
        for (i in 0 until src) {
            statObj.add(
                Statistics(
                    i.toLong() + 1,
                    sources[i].countApplications(),
                    devDeniedSources[i].toDouble() / devHandledSources[i],
                    timeInBuffer[i].toDouble() / countInBuff[i],
                    devTimeHandledSources[i].toDouble() / devHandledSources[i],
                    (devTimeHandledSources[i].toDouble() / devHandledSources[i]) + (timeInBuffer[i].toDouble() / countInBuff[i]),
                    1.2,
                    1.1

                )
            )
        }
    }
}