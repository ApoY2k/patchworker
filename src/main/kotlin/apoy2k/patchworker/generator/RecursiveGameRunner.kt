package apoy2k.patchworker.generator

import apoy2k.patchworker.game.*
import io.github.oshai.kotlinlogging.KotlinLogging
import javax.sql.DataSource

private val log = KotlinLogging.logger {}

private const val STATEMENT = """
    insert into data
     (
        patches,
        p1_tracker_position, p1_button_multiplier, p1_buttons, p1_special_patches, p1_actions_taken, p1_board,
        p2_tracker_position, p2_button_multiplier, p2_buttons, p2_special_patches, p2_actions_taken, p2_board,
        is_p1_turn,
        score
     )
     values
      (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
     on conflict do nothing
"""

fun runGame(
    game: Game,
    maxDepth: Int,
    depth: Int,
    db: DataSource
) {
    val currentPlayer = game.nextPlayer
    if (currentPlayer != null && depth < maxDepth) {
        spawnChildGames(game, currentPlayer, maxDepth, depth, db)
    } else {
        debug(depth, "Game end reached for $game")
        val score = scorePlayer(game.player1) - scorePlayer(game.player2)

        db.connection.use { connection ->
            connection.prepareStatement(STATEMENT).use { statement ->
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
                statement.setInt(15, score)
                statement.executeUpdate()
            }
        }
    }
}

private fun spawnChildGames(
    game: Game,
    player: Player,
    maxDepth: Int,
    depth: Int,
    db: DataSource
) {
    debug(depth, "Deciding moves for $player in $game")
    for (patch in game.getPatchOptions()) {
        for ((rowIdx, fields) in player.board.withIndex()) {
            for ((colIdx, _) in fields.withIndex()) {
                repeat(2) { flip ->
                    debug(depth, "Flip loop #$flip for $player in $game")
                    repeat(4) { rotate ->
                        debug(depth, "Rotate loop #$rotate for $player in $game")
                        val anchor = Position(rowIdx, colIdx)
                        val childGame = game.copy()
                        debug(depth, "Creating copy of $game as $childGame to run patch place")
                        runPlaceCopy(childGame, patch, anchor, maxDepth, depth, db)
                        patch.rotate()
                    }
                    patch.rotate()
                    patch.flip()
                }
            }
        }
    }

    val childGame = game.copy()
    debug(depth, "Creating copy of $game as $childGame to run advance")
    runAdvanceCopy(childGame, maxDepth, depth, db)
}

private fun runPlaceCopy(
    game: Game,
    patch: Patch,
    anchor: Pair<Int, Int>,
    maxDepth: Int,
    depth: Int,
    db: DataSource
) {
    debug(depth, "Placing $patch at $anchor for ${game.nextPlayer} in $game")
    if (game.place(patch, anchor)) {
        debug(depth, "Recursing into $game")
        runGame(game, maxDepth, depth + 1, db)
    }
}

private fun runAdvanceCopy(
    game: Game,
    maxDepth: Int,
    depth: Int,
    db: DataSource
) {
    debug(depth, "Advancing ${game.nextPlayer} in $game")
    game.advance()
    debug(depth, "Recursing into $game")
    runGame(game, maxDepth, depth + 1, db)
}

private fun debug(depth: Int, message: String) {
    log.debug { "   -".repeat(depth) + "> $message" }
}
