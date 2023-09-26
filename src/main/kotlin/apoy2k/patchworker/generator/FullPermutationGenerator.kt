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
        if (!scores.containsKey(game.stateChecksum())) {
            printDebug(depth, "Game end reached for $game")
            val lines = mutableListOf("$game -------------------------------------------")
            lines.addAll(renderGame(game))
            writer.write(lines.joinToString { "\r\n" }) // TODO doesnt work :(
            scores[game.stateChecksum()] = scorePlayer(game.player1) - scorePlayer(game.player2)
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
        player.board.forEachIndexed { rowIdx, fields ->
            fields.forEachIndexed { colIdx, _ ->
                repeat(1) { flip ->
                    printDebug(depth, "Flip loop #$flip for $player in $game")
                    repeat(3) { rotate ->
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

private fun renderGame(game: Game): List<String> {
    val result = mutableListOf<String>()
    result.add("- Player1: ${game.player1}")
    result.addAll(renderPlayer(game.player1))
    result.add("- Player2: ${game.player2}")
    result.addAll(renderPlayer(game.player2))
    return result
}

private fun renderPlayer(player: Player): List<String> {
    val result = mutableListOf<String>()
    player.board.forEach { row ->
        val line = StringBuilder()
        row.forEach { field ->
            line.append(
                when (field) {
                    true -> " X "
                    else -> "   "
                }
            )
        }
        result.add(line.toString())
    }
    result.add(
        "ActionsTaken:${player.actionsTaken} " +
                "Track:${player.trackerPosition} " +
                "Buttons:${player.buttons} " +
                "SpecialPatches:${player.specialPatches} " +
                "Score:${scorePlayer(player)}"
    )
    return result
}
