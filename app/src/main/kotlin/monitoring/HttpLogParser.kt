package monitoring

import monitoring.domain.HttpLog
import monitoring.domain.Request
import java.time.Instant

object HttpLogParser {
    private const val REMOTE_HOST: Int = 0
    private const val AUTH_USER: Int = 2
    private const val DATE: Int = 3
    private const val REQUEST: Int = 4
    private const val STATUS: Int = 5
    private const val BYTES: Int = 6

    fun parse(line: String): HttpLog {
        val tokens = line.split(",")
        val date = Instant.ofEpochSecond(tokens[DATE].toLong())
        val request = buildRequest(tokens)

        return HttpLog(
            tokens[REMOTE_HOST].removeSurrounding(delimiter = "\""),
            tokens[AUTH_USER].removeSurrounding(delimiter = "\""),
            date,
            request,
            Integer.parseInt(tokens[STATUS]),
            Integer.parseInt(tokens[BYTES]),
        )
    }

    private fun buildRequest(tokens: List<String>): Request {
        val request = tokens[REQUEST].removeSurrounding(delimiter = "\"").split(" ")
        val httpVerb = request[0]
        val httpResource = request[1].split('/')

        return Request(httpVerb, "/${httpResource[1]}")
    }
}
