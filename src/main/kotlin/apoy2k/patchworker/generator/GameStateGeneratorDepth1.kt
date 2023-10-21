package apoy2k.patchworker.generator

import apoy2k.patchworker.INSERT_DATA_STATEMENT
import apoy2k.patchworker.datasource
import apoy2k.patchworker.game.*
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.postgresql.util.PSQLException
import java.util.concurrent.Executors

private val parallelism = Runtime.getRuntime().availableProcessors()
private val log = KotlinLogging.logger {}
private val gameSimDispatcher = Executors.newFixedThreadPool(parallelism).asCoroutineDispatcher()

fun main() {
    runBlocking {
        repeat(parallelism) {
            launch(gameSimDispatcher) {
                runGame(Game())
            }
        }
        joinAll()
    }
}

fun runGame(game: Game) {
    val currentPlayer = game.nextPlayer
    if (currentPlayer != null) {
        log.debug { "Creating move option game copies for $currentPlayer in $game" }
        val options = createMoveOptionGameCopies(game, currentPlayer)

        val score = options
            .sumOf { scorePlayer(it.player1) - scorePlayer(it.player2) }
            .toFloat()
            .div(options.size)

        log.debug { "Saving game state of $game with score $score" }
        try {
            saveGame(game, score, "data_depth_1")
        } catch (ex: PSQLException) {
            // Truncate branches that are already duplicated in the database
            if (ex.sqlState == "23505") {
                return
            }
            throw ex
        }

        options.forEach { runGame(it) }
    }
}

private fun createMoveOptionGameCopies(game: Game, player: Player): List<Game> {
    val options = mutableListOf<Game>()

    for (patch in game.getPatchOptions()) {
        for ((rowIdx, fields) in player.board.withIndex()) {
            for ((colIdx, _) in fields.withIndex()) {
                repeat(2) {
                    repeat(4) {
                        val anchor = Position(rowIdx, colIdx)
                        val childGame = game.copy()
                        if (childGame.place(patch, anchor)) {
                            options.add(childGame)
                        }
                        patch.rotate()
                    }
                    patch.rotate()
                    patch.flip()
                }
            }
        }
    }

    val childGame = game.copy()
    options.add(childGame)
    childGame.advance()

    return options
}

private fun saveGame(game: Game, score: Float, table: String) {
    datasource.connection.use { connection ->
        val query = INSERT_DATA_STATEMENT.format(table)
        connection.prepareStatement(query).use { statement ->
            statement.setString(1, game.patchesChecksum())
            statement.setInt(2, game.player1.trackerPosition)
            statement.setInt(3, game.player1.buttonMultiplier)
            statement.setInt(4, game.player1.buttons)
            statement.setInt(5, game.player1.specialPatches)
            statement.setInt(6, game.player1.actionsTaken)
            statement.setString(7, game.player1.board.checksum())
            statement.setInt(8, game.player2.trackerPosition)
            statement.setInt(9, game.player2.buttonMultiplier)
            statement.setInt(10, game.player2.buttons)
            statement.setInt(11, game.player2.specialPatches)
            statement.setInt(12, game.player2.actionsTaken)
            statement.setString(13, game.player2.board.checksum())
            statement.setBoolean(14, game.isPlayer1Turn())
            statement.setFloat(15, score)
            statement.executeUpdate()
        }
    }
}
