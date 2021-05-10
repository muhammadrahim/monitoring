package monitoring

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
        val request = tokens[REQUEST]
        val section = request.split(" ")[1].split('/')[1]

        return HttpLog(
            tokens[REMOTE_HOST].substring(1, tokens[REMOTE_HOST].length - 1),
            tokens[AUTH_USER].substring(1, tokens[AUTH_USER].length - 1),
            date,
            request.substring(1, request.length - 1),
            Integer.parseInt(tokens[STATUS]),
            Integer.parseInt(tokens[BYTES]),
            section
        )
    }
}
