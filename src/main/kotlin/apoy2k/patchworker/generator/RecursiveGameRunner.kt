package apoy2k.patchworker.generator

import apoy2k.patchworker.game.*
import io.github.oshai.kotlinlogging.KotlinLogging
import java.sql.Connection

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
    connection: Connection
) {
    val currentPlayer = game.nextPlayer
    if (currentPlayer != null && depth < maxDepth) {
        spawnChildGames(game, currentPlayer, maxDepth, depth, connection)
    } else {
        debug(depth, "Game end reached for $game")
        val score = scorePlayer(game.player1) - scorePlayer(game.player2)

        connection.prepareStatement(STATEMENT).use {
            it.setString(1, game.patchesChecksum())
            it.setInt(2, game.player1.trackerPosition)
            it.setInt(3, game.player1.buttonMultiplier)
            it.setInt(4, game.player1.buttons)
            it.setInt(5, game.player1.specialPatches)
            it.setInt(6, game.player1.actionsTaken)
            it.setString(7, game.player1.board.checksum())
            it.setInt(8, game.player2.trackerPosition)
            it.setInt(9, game.player2.buttonMultiplier)
            it.setInt(10, game.player2.buttons)
            it.setInt(11, game.player2.specialPatches)
            it.setInt(12, game.player2.actionsTaken)
            it.setString(13, game.player2.board.checksum())
            it.setBoolean(14, game.isPlayer1Turn())
            it.setInt(15, score)
            it.executeUpdate()
        }
    }
}

private fun spawnChildGames(
    game: Game,
    player: Player,
    maxDepth: Int,
    depth: Int,
    connection: Connection
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
                        runPlaceCopy(childGame, patch, anchor, maxDepth, depth, connection)
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
    runAdvanceCopy(childGame, maxDepth, depth, connection)
}

private fun runPlaceCopy(
    game: Game,
    patch: Patch,
    anchor: Pair<Int, Int>,
    maxDepth: Int,
    depth: Int,
    connection: Connection
) {
    debug(depth, "Placing $patch at $anchor for ${game.nextPlayer} in $game")
    if (game.place(patch, anchor)) {
        debug(depth, "Recursing into $game")
        runGame(game, maxDepth, depth + 1, connection)
    }
}

private fun runAdvanceCopy(
    game: Game,
    maxDepth: Int,
    depth: Int,
    connection: Connection
) {
    debug(depth, "Advancing ${game.nextPlayer} in $game")
    game.advance()
    debug(depth, "Recursing into $game")
    runGame(game, maxDepth, depth + 1, connection)
}

private fun debug(depth: Int, message: String) {
    log.debug { "   -".repeat(depth) + "> $message" }
}
