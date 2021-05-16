/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package monitoring

import java.io.BufferedReader
import java.io.FileReader
import java.nio.file.FileSystems

fun main(args: Array<String>) {
    val bufferedReader = BufferedReader(
        FileReader(
            FileSystems.getDefault().getPath(args[0]).normalize().toAbsolutePath().toString()
        )
    )

    val threshold = if (args.size > 1) {
        Integer.parseInt(args[1])
    } else 10

    val dataRepository = DataRepository()

    HttpLogMonitor(dataRepository, bufferedReader).handleLogs(threshold)
}
