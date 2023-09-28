package apoy2k.patchworker.generator

import apoy2k.patchworker.game.*
import apoy2k.patchworker.writer
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

private val log = KotlinLogging.logger {}

fun runGame(scores: ConcurrentHashMap<String, Int>, game: Game, maxDepth: Int, depth: Int) {
    val currentPlayer = game.nextPlayer
    if (currentPlayer != null && depth < maxDepth) {
        spawnChildGames(scores, game, currentPlayer, maxDepth, depth)
    } else {
        if (!scores.containsKey(game.checksum())) {
            printDebug(depth, "Game end reached for $game")
            val score = scorePlayer(game.player1) - scorePlayer(game.player2)
            val lines = StringBuilder()
                .append(game.checksum())
                .append(",")
                .append(score)
                .append("\r\n")
            writer.write(lines.toString())
            scores[game.checksum()] = score
        }
    }
    writer.flush()
}

private fun spawnChildGames(
    scores: ConcurrentHashMap<String, Int>,
    game: Game,
    player: Player,
    maxDepth: Int,
    depth: Int
) {
    printDebug(depth, "Deciding moves for $player in $game")
    for (patch in game.getPatchOptions()) {
        for ((rowIdx, fields) in player.board.withIndex()) {
            for ((colIdx, _) in fields.withIndex()) {
                repeat(2) { flip ->
                    printDebug(depth, "Flip loop #$flip for $player in $game")
                    repeat(4) { rotate ->
                        printDebug(depth, "Rotate loop #$rotate for $player in $game")
                        val anchor = Position(rowIdx, colIdx)
                        val childGame = game.copy()
                        printDebug(depth, "Creating copy of $game as $childGame to run patch place")
                        runPlaceCopy(scores, childGame, patch, anchor, maxDepth, depth)
                        patch.rotate()
                    }
                    patch.rotate()
                    patch.flip()
                }
            }
        }
    }

    val childGame = game.copy()
    printDebug(depth, "Creating copy of $game as $childGame to run advance")
    runAdvanceCopy(scores, childGame, maxDepth, depth)
}

private fun runPlaceCopy(
    scores: ConcurrentHashMap<String, Int>,
    game: Game,
    patch: Patch,
    anchor: Pair<Int, Int>,
    maxDepth: Int,
    depth: Int
) {
    printDebug(depth, "Placing $patch at $anchor for ${game.nextPlayer} in $game")
    if (game.place(patch, anchor)) {
        printDebug(depth, "Recursing into $game")
        runGame(scores, game, maxDepth, depth + 1)
    }
}

private fun runAdvanceCopy(
    scores: ConcurrentHashMap<String, Int>,
    game: Game,
    maxDepth: Int,
    depth: Int
) {
    printDebug(depth, "Advancing ${game.nextPlayer} in $game")
    game.advance()
    printDebug(depth, "Recursing into $game")
    runGame(scores, game, maxDepth, depth + 1)
}

private fun printDebug(depth: Int, message: String) {
    log.debug { "   -".repeat(depth) + "> $message" }
}
