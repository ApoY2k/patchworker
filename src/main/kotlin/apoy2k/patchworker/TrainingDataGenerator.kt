package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.Position
import apoy2k.patchworker.game.scorePlayer
import java.util.concurrent.ConcurrentHashMap

fun simulateGameStep(scores: ConcurrentHashMap<Int, Int>, game: Game, maxDepth: Int, depth: Int = 0) {
    val player = game.nextPlayer

    if (depth > maxDepth || player == null) {
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
                            simulateGameStep(scores, childGame, maxDepth, depth + 1)
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
        simulateGameStep(scores, childGame, maxDepth, depth + 1)
    }
}
