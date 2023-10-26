package apoy2k.patchworker.generator

import apoy2k.patchworker.datasource
import apoy2k.patchworker.game.Game
import apoy2k.patchworker.scoring.EndGameScorer
import apoy2k.patchworker.scoring.GameStateScorer
import apoy2k.patchworker.scoring.ModelScorer
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

private val parallelism = Runtime.getRuntime().availableProcessors()
private val gameSimDispatcher = Executors.newFixedThreadPool(parallelism).asCoroutineDispatcher()

fun main(vararg args: String) {
    when (val depth = args[0].toInt()) {
        1 -> generate("data_depth_$depth", EndGameScorer())
        else -> generate("data_depth_$depth", ModelScorer("data_depth_${depth - 1}"))
    }
}

private fun generate(table: String, scorer: GameStateScorer) {
    val createTableQuery = scorer.javaClass.getResource("/create_table.sql")?.readText().orEmpty()

    datasource.connection.use { connection ->
        connection.createStatement().use { statement ->
            statement.execute(createTableQuery.format(table, table, table, table, table))
        }
    }

    val generator = GameStateGenerator(table, scorer)
    runBlocking {
        repeat(parallelism) {
            launch(gameSimDispatcher) {
                generator.generate(Game(0))
            }
        }
        joinAll()
    }
}
