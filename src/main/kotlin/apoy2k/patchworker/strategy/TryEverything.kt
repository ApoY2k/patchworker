package apoy2k.patchworker.strategy

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.Patch
import apoy2k.patchworker.game.Player
import apoy2k.patchworker.game.Position

class TryEverything : Strategy {
    override fun execute(game: Game) {
        val player = game.nextPlayer ?: throw NullPointerException()
        val patches = game.getPatchOptions(player)
        var wasPlaced = false

        for (patch in patches) {
            if (patch.buttonCost > player.buttons) {
                continue
            }

            wasPlaced = tryPlaceOnBoard(game, player, patch)
            if (wasPlaced) {
                break
            }
        }

        if (!wasPlaced) {
            game.advance(player)
        }
    }

    private fun tryPlaceOnBoard(
        game: Game,
        player: Player,
        patch: Patch
    ): Boolean {
        repeat(2) {
            repeat(3) {
                player.board.forEachIndexed { rowIdx, fields ->
                    fields.forEachIndexed { colIdx, _ ->
                        val anchor = Position(rowIdx, colIdx)
                        val wasPlaced = game.place(player, patch, anchor)
                        if (wasPlaced) {
                            return true
                        }
                    }
                }
                patch.rotate()
            }
            patch.flip()
        }
        return false
    }
}
