package monitoring

import monitoring.domain.HttpLog
import java.time.Instant
import java.util.*

class DataRepository(
    val tenSecondLogs: Queue<HttpLog> = PriorityQueue(compareBy { it.date }),
    val twoMinuteLogs: Queue<HttpLog> = PriorityQueue(compareBy { it.date }),
    private val sectionActivity: MutableMap<String, Int> = mutableMapOf(),
    private var clientErrors: Int = 0,
    private var serverErrors: Int = 0
) {

    fun addData(httpLog: HttpLog) {
        sectionData(httpLog)
        erroneousStatusCodes(httpLog)
        tenSecondLogs.add(httpLog)
        twoMinuteLogs.add(httpLog)
    }

    private fun sectionData(httpLog: HttpLog) {
        val key = httpLog.request.resourceSection
        sectionActivity[key] = sectionActivity[key]?.plus(1) ?: 1
    }

    private fun erroneousStatusCodes(httpLog: HttpLog) {
        when (httpLog.status) {
            in 400..499 -> clientErrors++
            in 500..599 -> serverErrors++
        }
    }

    fun resetTenSecondWindow() {
        tenSecondLogs.clear()
        sectionActivity.clear()
        serverErrors = 0
        clientErrors = 0
    }

    fun reduceToTwoMinWindow() {
        val mostRecentLogTimestamp = twoMinuteLogs.last().date.epochSecond
        twoMinuteLogs.removeAll { mostRecentLogTimestamp - it.date.epochSecond >= 120 }
    }

    fun getStatistics(initTimestamp: Instant): String {
        val mostActiveSection = sectionActivity.maxByOrNull { it.value }

        return "$initTimestamp:        " +
            "most active section=${mostActiveSection?.key} (${mostActiveSection?.value} hits). " +
            "10s traffic=${tenSecondLogs.size}. " +
            "4xx=$clientErrors. " +
            "5xx=$serverErrors."
    }
}
