package monitoring

import monitoring.domain.HttpLog
import monitoring.domain.Request
import java.time.Instant

object TestHttpLogBuilder {
    fun build(
        remoteHost: String = "127.0.0.1",
        authUser: String = "authUser",
        timestamp: Instant = Instant.now(),
        request: Request = Request("GET", "/api"),
        status: Int = 200,
        bytes: Int = 1234
    ): HttpLog = HttpLog(remoteHost, authUser, timestamp, request, status, bytes)
}
