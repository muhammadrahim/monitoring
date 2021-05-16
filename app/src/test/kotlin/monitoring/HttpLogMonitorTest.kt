package monitoring

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import monitoring.AlertStatus.AlreadyInactive
import monitoring.domain.HttpLog
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*

internal class HttpLogMonitorTest {

    private val path = "src/test/resources"
    private val file = File(path)
    private val bufferedReader = BufferedReader(FileReader("${file.absolutePath}/sample_csv.txt"))

    @Test
    fun `should call data repository`() {
        val dataRepository = mockk<DataRepository>()
        val httpLogMonitor = HttpLogMonitor(dataRepository, bufferedReader)
        val priorityQueue = PriorityQueue<HttpLog>(compareBy { it.date })
        mockRepository(dataRepository, priorityQueue)

        httpLogMonitor.handleLogs()

        verify(exactly = 5) { dataRepository.addData(any()) }
        verify(exactly = 5) { dataRepository.reduceToTwoMinWindow() }
        verify(exactly = 1) { dataRepository.resetTenSecondWindow() }
        verify(exactly = 1) { dataRepository.getStatistics(any()) }
    }

    @Test
    fun `should call alert handler`() {
        val dataRepository = mockk<DataRepository>()
        val httpLogMonitor = HttpLogMonitor(dataRepository, bufferedReader)
        val element = TestHttpLogBuilder.build()
        val priorityQueue = PriorityQueue<HttpLog>(compareBy { it.date }).also { it.add(element) }
        mockRepository(dataRepository, priorityQueue)
        mockkObject(AlertHandler)

        httpLogMonitor.handleLogs()

        assertEquals(
            AlreadyInactive,
            AlertHandler.handle(dataRepository.twoMinuteLogs, 10 * 120, false, element.date)
        )
    }

    private fun mockRepository(dataRepository: DataRepository, priorityQueue: PriorityQueue<HttpLog>) {
        every { dataRepository.addData(any()) } just Runs
        every { dataRepository.reduceToTwoMinWindow() } just Runs
        every { dataRepository.resetTenSecondWindow() } just Runs
        every { dataRepository.twoMinuteLogs } returns priorityQueue
        every { dataRepository.getStatistics(any()) } returns "some string"
    }
}
