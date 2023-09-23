package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.Player
import apoy2k.patchworker.game.Position
import apoy2k.patchworker.game.scorePlayer
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.Path
import kotlin.io.path.writeLines

val dataFile = Files.createFile(Path("data.txt"))

fun simulateStep(scores: ConcurrentHashMap<Int, Int>, game: Game, maxDepth: Int, depth: Int = 0) {
    val player = game.nextPlayer

    if (depth > maxDepth || player == null) {
        dataFile.writeLines(
            listOf("------------------------------------------------"),
            Charsets.UTF_8,
            StandardOpenOption.APPEND
        )
        dataFile.writeLines(renderGame(game), Charsets.UTF_8, StandardOpenOption.APPEND)
        scores[game.hashCode()] = scorePlayer(game.player1) - scorePlayer(game.player2)
        return
    }

    val patches = game.getPatchOptions(player)
        .filter { it.buttonCost <= player.buttons }

    var wasPlaced = false
    for (patch in patches) {
        repeat(2) {
            repeat(3) {
                player.board.forEachIndexed { rowIdx, fields ->
                    fields.forEachIndexed { colIdx, _ ->
                        val anchor = Position(rowIdx, colIdx)
                        wasPlaced = game.place(player, patch, anchor)
                        if (wasPlaced) {
                            val childGame = game.copy()
                            simulateStep(scores, childGame, maxDepth, depth + 1)
                        }
                    }
                }
                patch.rotate()
            }
            patch.flip()
        }

        if (wasPlaced) {
            break
        }
    }

    if (!wasPlaced) {
        game.advance(player)
        val childGame = game.copy()
        simulateStep(scores, childGame, maxDepth, depth + 1)
    }
}

private fun renderGame(game: Game): List<String> {
    val result = mutableListOf<String>()
    result.add("- Player1")
    result.addAll(renderPlayer(game.player1))
    result.add("- Player2")
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
