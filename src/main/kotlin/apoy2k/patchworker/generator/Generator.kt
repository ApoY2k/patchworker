package apoy2k.patchworker.generator

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.scoring.EndGameScorer
import apoy2k.patchworker.scoring.GameStateScorer
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

private val parallelism = Runtime.getRuntime().availableProcessors()
private val gameSimDispatcher = Executors.newFixedThreadPool(parallelism).asCoroutineDispatcher()

private const val TABLE = "data_depth_1"

fun main() {
    generate("data_depth_1", EndGameScorer())
//    generate(TABLE, ModelScorer(TABLE))
}

private fun generate(table: String, scorer: GameStateScorer) {
    val generator = GameStateGenerator(table, scorer)
    runBlocking {
        repeat(parallelism) {
            launch(gameSimDispatcher) {
                generator.generate(Game())
            }
        }
        joinAll()
    }
}
