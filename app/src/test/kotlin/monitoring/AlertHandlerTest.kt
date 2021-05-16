package monitoring

import monitoring.AlertStatus.Activated
import monitoring.AlertStatus.AlreadyActive
import monitoring.AlertStatus.AlreadyInactive
import monitoring.AlertStatus.Deactivated
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

internal class AlertHandlerTest {

    @Test
    fun `should trigger an alert if threshold reached and alert is off`() {
        val list = listOf(TestHttpLogBuilder.build())

        val actual = AlertHandler.handle(list, 0, alert = false, Instant.now())

        assertEquals(Activated, actual)
    }

    @Test
    fun `should not trigger an alert if threshold reached but alert is already on`() {
        val list = listOf(TestHttpLogBuilder.build())

        val actual = AlertHandler.handle(list, 0, alert = true, Instant.now())

        assertEquals(AlreadyActive, actual)
    }

    @Test
    fun `should not trigger an alert if threshold is not reached`() {

        val actual = AlertHandler.handle(emptyList(), 1, alert = false, Instant.now())

        assertEquals(AlreadyInactive, actual)
    }
    @Test
    fun `should publish recovery if alert is on but threshold is no longer met`() {

        val actual = AlertHandler.handle(emptyList(), 1, alert = true, Instant.now())

        assertEquals(Deactivated, actual)
    }
}
