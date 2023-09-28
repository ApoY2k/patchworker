package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.generator.runGame
import kotlinx.coroutines.runBlocking
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter

val dataFile = Path("data/generated_states.txt")
val writer = dataFile.bufferedWriter(Charsets.UTF_8, DEFAULT_BUFFER_SIZE, StandardOpenOption.CREATE)

fun main() {
    runBlocking {
        val results = ConcurrentHashMap<String, Int>()

        thread {
            while (true) {
                Thread.sleep(100)
                print("\rAverage score: ${results.values.average()} (${results.size} unique end states)")
            }
        }

        for (maxDepth in 1..50) {
            thread {
                val game = Game()
                runGame(results, game, maxDepth, 0)
            }
        }
    }
}
