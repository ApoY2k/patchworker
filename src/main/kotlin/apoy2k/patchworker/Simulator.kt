package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.Player
import apoy2k.patchworker.game.Position
import apoy2k.patchworker.game.scorePlayer
import org.slf4j.LoggerFactory
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.writeLines

val log = LoggerFactory.getLogger("Simulator")

fun simulateStep(scores: ConcurrentHashMap<Int, Int>, game: Game, maxDepth: Int, depth: Int = 0) {
    if (depth > maxDepth) {
        return
    }

    var currentPlayer: Player? = game.nextPlayer ?: return
    while (currentPlayer != null) {
        val patches = game.getPatchOptions(currentPlayer)
            .filter { it.buttonCost <= currentPlayer!!.buttons }

        printDebug(depth, "Deciding moves for $currentPlayer in $game")

        var wasPlaced = false
        for (patch in patches) {
            run placing@{
                repeat(2) { flip ->
                    printDebug(depth, "Flip loop #$flip for $currentPlayer in $game")
                    repeat(3) { rotate ->
                        printDebug(depth, "Rotate loop #$rotate for $currentPlayer in $game")
                        currentPlayer!!.board.forEachIndexed { rowIdx, fields ->
                            fields.forEachIndexed { colIdx, _ ->
                                val anchor = Position(rowIdx, colIdx)
                                printDebug(depth, "Trying to place $patch at $anchor for $currentPlayer in $game")
                                wasPlaced = game.place(currentPlayer!!, patch, anchor)
                                if (wasPlaced) {
                                    val childGame = game.copy()
                                    printDebug(depth, "Stepping into new game copy of $game ---> $childGame")
                                    simulateStep(scores, childGame, maxDepth, depth + 1)
                                    // The patch was placed, so the iteration over all possible positions
                                    // to place the patch at can be broken at this point
                                    return@placing
                                }
                            }
                        }
                        patch.rotate()
                    }
                    patch.flip()
                }
            }

            // A patch was placed, so don't try to place another one
            if (wasPlaced) {
                break
            }
        }

        if (!wasPlaced) {
            printDebug(depth, "Advancing $currentPlayer in $game")
            game.advance(currentPlayer)
            val childGame = game.copy()
            printDebug(depth, "Stepping into new game copy of $game as $childGame")
            simulateStep(scores, childGame, maxDepth, depth + 1)
        }

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
