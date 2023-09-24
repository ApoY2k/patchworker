package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists

val dataFile = Path("data.txt")

fun main() {
    dataFile.deleteIfExists()
    dataFile.createFile()

    runBlocking {
        var finished = false
        val results = ConcurrentHashMap<Int, Int>()

        thread {
            while (!finished) {
                Thread.sleep(100)
//                print("\rAverage score: ${results.values.average()} (${results.size} states)")
            }
        }

        thread {
            val game = Game()
            simulateStep(results, game, 10)
            finished = true
        }
    }
}
