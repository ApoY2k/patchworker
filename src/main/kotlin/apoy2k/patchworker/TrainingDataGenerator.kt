package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.Position
import apoy2k.patchworker.game.scorePlayer

fun recurseGameSteps(game: Game, depth: Int, maxDepth: Int) {
    if (depth >= maxDepth) {
        println(
            "$game finished. ${game.getRemainingPatches()} patches remain. " +
                    "${game.player1}: ${scorePlayer(game.player1)} / " +
                    "${game.player2}: ${scorePlayer(game.player2)}"
        )
        return
    }

    val player = game.nextPlayer ?: return
    val patches = game.getPatchOptions(player)
    var wasPlaced = false

    for (patch in patches) {
        if (patch.buttonCost > player.buttons) {
            continue
        }

        repeat(2) mutations@{
            repeat(3) {
                player.board.forEachIndexed { rowIdx, fields ->
                    fields.forEachIndexed { colIdx, _ ->
                        val anchor = Position(rowIdx, colIdx)
                        wasPlaced = game.place(player, patch, anchor)
                        val childGame = game.copy()
                        recurseGameSteps(childGame, depth + 1, maxDepth)
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
    }
}
