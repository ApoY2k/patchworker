package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

fun main() {
    runBlocking {
        var finished = false
        val results = mutableSetOf<Int>()
        thread {
            while (!finished) {
                Thread.sleep(100)
                print("\rAverage score: ${results.average()} (${results.size} games)")
            }
        }
        thread {
            val game = Game()
            simulateGameStep(results, game, 5)
            finished = true
        }
    }
}
