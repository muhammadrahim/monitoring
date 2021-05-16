package monitoring

import monitoring.AlertStatus.Activated
import monitoring.AlertStatus.AlreadyActive
import monitoring.AlertStatus.AlreadyInactive
import monitoring.AlertStatus.Deactivated
import monitoring.domain.HttpLog
import java.io.BufferedReader
import java.time.Instant

class HttpLogMonitor(private val dataRepository: DataRepository, private val bufferedReader: BufferedReader) {

    init {
        bufferedReader.readLine() // remove csv header 
    }

    fun handleLogs(requestsPerSecondThreshold: Int = TEN) {
        var timestamp: Long? = null
        var currentAlertStatus = false

        bufferedReader.forEachLine {
            val currentHttpLog = HttpLogParser.parse(it)
            timestamp = timestamp ?: currentHttpLog.date.epochSecond

            dataRepository.addData(currentHttpLog)

            if (currentHttpLog.date.epochSecond - timestamp!! >= TEN) {
                timestamp = timestamp!! + TEN
                println(dataRepository.getStatistics(Instant.ofEpochSecond(timestamp!!)))
                dataRepository.resetTenSecondWindow()
            }

            dataRepository.reduceToTwoMinWindow()

            currentAlertStatus = alertHandling(requestsPerSecondThreshold, currentAlertStatus, currentHttpLog)
        }
    }

    private fun alertHandling(requestsPerSecondThreshold: Int, alert: Boolean, httpLog: HttpLog): Boolean =
        AlertHandler.handle(
            dataRepository.twoMinuteLogs,
            requestsPerSecondThreshold * TWO_MINUTES,
            alert,
            httpLog.date
        ).let { alertStatus ->
            when (alertStatus) {
                Activated, AlreadyActive -> true
                Deactivated, AlreadyInactive -> false
            }
        }

    companion object {
        private const val TEN = 10
        private const val TWO_MINUTES = 120
    }
}
