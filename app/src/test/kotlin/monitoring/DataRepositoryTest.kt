package monitoring

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

internal class DataRepositoryTest {

    @Test
    fun `should update 5xx errors correctly when given a http log`() {
        val dataRepository = DataRepository()
        val httpLog = TestHttpLogBuilder.build(status = 500)

        dataRepository.addData(httpLog)
        val actual = dataRepository.getStatistics(Instant.now())

        assert(actual.contains("5xx=1"))
    }

    @Test
    fun `should update 4xx errors correctly when given a http log`() {
        val dataRepository = DataRepository()
        val httpLog = TestHttpLogBuilder.build(status = 400)

        dataRepository.addData(httpLog)
        val actual = dataRepository.getStatistics(Instant.now())

        assert(actual.contains("4xx=1"))
    }

    @Test
    fun `should reset data for the ten second window but not the past two minutes`() {
        val dataRepository = DataRepository()
        val httpLog = TestHttpLogBuilder.build(timestamp = Instant.now())
        dataRepository.addData(httpLog)

        dataRepository.resetTenSecondWindow()

        assertEquals(dataRepository.tenSecondLogs.size, 0)
        assertEquals(dataRepository.twoMinuteLogs.size, 1)
    }

    @Test
    fun `should remove data older than two minutes`() {
        val dataRepository = DataRepository()
        val timestamp = Instant.now()
        val httpLog = TestHttpLogBuilder.build(timestamp = timestamp.minusSeconds(120L))
        val httpLog2 = TestHttpLogBuilder.build(timestamp = timestamp)
        dataRepository.addData(httpLog)
        dataRepository.addData(httpLog)
        dataRepository.addData(httpLog2)

        dataRepository.reduceToTwoMinWindow()

        assertEquals(dataRepository.twoMinuteLogs.size, 1)
    }

    @Test
    fun `should store data in order by timestamp`() {
        val dataRepository = DataRepository()
        val httpLog = TestHttpLogBuilder.build(timestamp = Instant.now())
        val httpLog2 = TestHttpLogBuilder.build(timestamp = Instant.now())

        dataRepository.addData(httpLog)
        dataRepository.addData(httpLog2)

        assertEquals(dataRepository.tenSecondLogs.poll(), httpLog)
        assertEquals(dataRepository.tenSecondLogs.poll(), httpLog2)

        assertEquals(dataRepository.twoMinuteLogs.poll(), httpLog)
        assertEquals(dataRepository.twoMinuteLogs.poll(), httpLog2)
    }
}
