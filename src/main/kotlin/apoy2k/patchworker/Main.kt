package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.generator.runGame
import kotlinx.coroutines.runBlocking
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists

val dataFile = Path("data/generated_states.txt")
val writer = dataFile.bufferedWriter(Charsets.UTF_8, DEFAULT_BUFFER_SIZE, StandardOpenOption.APPEND)

fun main() {
    dataFile.deleteIfExists()
    dataFile.createFile()

    runBlocking {
        var finished = false
        val results = ConcurrentHashMap<Game, Int>()

        thread {
            while (!finished) {
                Thread.sleep(100)
                print("\rAverage score: ${results.values.average()} (${results.size} unique end states)")
            }
        }

        for (maxDepth in 1..50) {
            val game = Game()
            runGame(results, game, maxDepth, 0)
            finished = true
        }
    }
}
