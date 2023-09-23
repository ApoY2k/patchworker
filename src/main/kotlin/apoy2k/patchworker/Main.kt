package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

fun main() {
    runBlocking {
        var finished = false
        val results = ConcurrentHashMap<Int, Int>()

        thread {
            while (!finished) {
                Thread.sleep(100)
                print("\rAverage score: ${results.values.average()} (${results.size} games)")
            }
        }

        thread {
            val game = Game()
            simulateGameStep(results, game, 10)
            finished = true
        }
    }
}
