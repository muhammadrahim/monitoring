package monitoring

import java.time.Instant

data class HttpLog(
    val remoteHost: String,
    val authUser: String,
    val date: Instant,
    val request: String,
    val status: Int,
    val bytes: Int,
    val section: String
)
