package apoy2k.patchworker.strategy

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.Patch
import apoy2k.patchworker.game.Player
import apoy2k.patchworker.game.Position

class TryEverything : Strategy {
    override fun execute(game: Game) {
        val player = game.nextPlayer ?: throw NullPointerException()
        val patches = game.getPatchOptions()
        var wasPlaced = false

        patches.forEach { patch ->
            if (patch.buttonCost > player.buttons) {
                return@forEach
            }

            wasPlaced = tryPlaceOnBoard(game, player, patch)
            if (wasPlaced) {
                return@forEach
            }
            patch.flip()
            wasPlaced = tryPlaceOnBoard(game, player, patch)
            if (wasPlaced) {
                return@forEach
            }
        }

        if (!wasPlaced) {
            println("Advancing Player $player")
            game.advance(player)
        } else {
            println("Player $player has placed a patch")
        }
    }

    private fun tryPlaceOnBoard(
        game: Game,
        player: Player,
        patch: Patch
    ): Boolean {
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
            patch.flip()
        }
        return false
    }
}
