package monitoring.domain

import java.time.Instant

data class HttpLog(
    val remoteHost: String,
    val authUser: String,
    val date: Instant,
    val request: Request,
    val status: Int,
    val bytes: Int
)
