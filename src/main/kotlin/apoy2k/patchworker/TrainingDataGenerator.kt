package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.Position
import apoy2k.patchworker.game.scorePlayer
import kotlin.math.abs

fun recurseGameSteps(game: Game, depth: Int, maxDepth: Int) {
    val player = game.nextPlayer

    if (depth > maxDepth || player == null) {
        val scorePlayer1 = scorePlayer(game.player1)
        val scorePlayer2 = scorePlayer(game.player2)
        val fitnesse = abs(scorePlayer2) - abs(scorePlayer1)
        print("-".repeat(depth - 1))
        println(" $game finished. final score: $scorePlayer1 - $scorePlayer2. fitnesse : $fitnesse")
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
                            recurseGameSteps(childGame, depth + 1, maxDepth)
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
        recurseGameSteps(childGame, depth + 1, maxDepth)
    }
}
