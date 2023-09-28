package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.generator.runGame
import kotlinx.coroutines.runBlocking
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter

val dataFile = Path("data/generated_end_states.csv")
val writer = dataFile.bufferedWriter(Charsets.UTF_8, DEFAULT_BUFFER_SIZE, StandardOpenOption.CREATE)

fun main() {
    writer.write("PATCHES,PLAYER1_TRACKER_POS,PLAYER1_MULTIPLIER,PLAYER1_BUTTONS,PLAYER1_SPECIALPATCHES,PLAYER1_ACTIONS,PLAYER1_BOARD,PLAYER2_TRACKER_POS,PLAYER2_MULTIPLIER,PLAYER2_BUTTONS,PLAYER2_SPECIALPATCHES,PLAYER2_ACTIONS,PLAYER2_BOARD,IS_PLAYER1_TURN,SCORE\r\n")
    writer.flush()

    runBlocking {
        val results = ConcurrentHashMap<String, Int>()

        thread {
            while (true) {
                Thread.sleep(100)
                print("\r${results.size} end states collected")
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
