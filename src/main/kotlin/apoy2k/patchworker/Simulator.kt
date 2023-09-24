package apoy2k.patchworker

import apoy2k.patchworker.game.*
import org.slf4j.LoggerFactory
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.writeLines

val log = LoggerFactory.getLogger("Simulator")

fun runGame(scores: ConcurrentHashMap<Int, Int>, game: Game, maxDepth: Int, depth: Int = 0) {
    var currentPlayer: Player? = game.nextPlayer
    while (currentPlayer != null && depth < maxDepth) {
        runPlayer(scores, game, currentPlayer, maxDepth, depth)
        currentPlayer = game.nextPlayer
    }

    printDebug(depth, "Game end or max depth reached for $game")
    dataFile.writeLines(
        listOf("$game ------------------------------------------------"),
        Charsets.UTF_8,
        StandardOpenOption.APPEND
    )
    dataFile.writeLines(renderGame(game), Charsets.UTF_8, StandardOpenOption.APPEND)
    scores[game.hashCode()] = scorePlayer(game.player1) - scorePlayer(game.player2)
}

private fun runPlayer(scores: ConcurrentHashMap<Int, Int>, game: Game, player: Player, maxDepth: Int, depth: Int = 0) {
    printDebug(depth, "Deciding moves for $player in $game")
    for (patch in game.getPatchOptions()) {
        repeat(2) { flip ->
            printDebug(depth, "Flip loop #$flip for $player in $game")
            repeat(3) { rotate ->
                printDebug(depth, "Rotate loop #$rotate for $player in $game")
                player.board.forEachIndexed { rowIdx, fields ->
                    fields.forEachIndexed { colIdx, _ ->
                        val anchor = Position(rowIdx, colIdx)
                        val childGame = game.copy()
                        printDebug(depth, "Creating copy of $game as $childGame to run patch place")
                        runPlace(scores, childGame, patch, anchor, maxDepth, depth)
                    }
                }
                patch.rotate()
            }
            patch.flip()
        }
    }

    val childGame = game.copy()
    printDebug(depth, "Creating copy of $game as $childGame to run advance")
    runAdvance(scores, childGame, maxDepth, depth)
}

private fun runPlace(
    scores: ConcurrentHashMap<Int, Int>,
    game: Game,
    patch: Patch,
    anchor: Pair<Int, Int>,
    maxDepth: Int,
    depth: Int = 0
) {
    printDebug(depth, "Placing $patch at $anchor for ${game.nextPlayer} in $game")
    if (game.place(patch, anchor)) {
        printDebug(depth, "Recursing into $game")
        runGame(scores, game, maxDepth, depth + 1)
    }
}

private fun runAdvance(
    scores: ConcurrentHashMap<Int, Int>,
    game: Game,
    maxDepth: Int,
    depth: Int = 0
) {
    printDebug(depth, "Advancing ${game.nextPlayer} in $game")
    game.advance()
    printDebug(depth, "Recursing into $game")
    runGame(scores, game, maxDepth, depth + 1)
}

private fun printDebug(depth: Int, message: String) {
    log.debug("   -".repeat(depth) + "> {}", message)
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
