package monitoring

import monitoring.AlertStatus.Activated
import monitoring.AlertStatus.AlreadyActive
import monitoring.AlertStatus.AlreadyInactive
import monitoring.AlertStatus.Deactivated
import monitoring.domain.HttpLog
import java.time.Instant

object AlertHandler {

    fun handle(collection: Collection<HttpLog>, threshold: Int, alert: Boolean, time: Instant): AlertStatus =
        when {
            collection.size >= threshold && !alert -> Activated.also { alertHighTraffic(collection.size, time) }
            collection.size >= threshold && alert -> AlreadyActive
            collection.size < threshold && !alert -> AlreadyInactive
            collection.size < threshold && alert -> Deactivated.also { publishRecovery(time) }
            else -> throw IllegalStateException("unexpected state")
        }

    private fun publishRecovery(time: Instant) {
        println("$time:        Alert recovered")
    }

    private fun alertHighTraffic(value: Int, time: Instant) =
        println("High traffic generated an alert - hits = $value, triggered at $time")
}

sealed class AlertStatus {
    object AlreadyActive : AlertStatus()
    object Activated : AlertStatus()
    object Deactivated : AlertStatus()
    object AlreadyInactive : AlertStatus()
}
