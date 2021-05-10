package monitoring

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

internal class HttpLogParserTest {

    @Test
    fun `should correctly parse logs from csv file into the domain`() {
        val expected = HttpLog(
            remoteHost = "10.0.0.2",
            authUser = "apache",
            date = Instant.ofEpochSecond(1549573860),
            request = "GET /api/user HTTP/1.0",
            status = 200,
            bytes = 1234git add ,
            section = "api"
        )
        val logEntry = """"10.0.0.2","-","apache",1549573860,"GET /api/user HTTP/1.0",200,1234"""

        val actual = HttpLogParser.parse(logEntry)

        assertEquals(expected, actual)
    }
}
