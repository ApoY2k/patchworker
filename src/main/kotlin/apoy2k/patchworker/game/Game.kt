package apoy2k.patchworker.game

import java.util.*

class Game(
    private val patches: MutableList<Patch> = generatePatches()
) {
    val player1 = Player("player1")
    val player2 = Player("player2")

    var nextPlayer: Player? = player1
        private set

    fun getRemainingPatches() = patches.size

    fun getPatchOptions(player: Player) = when (player.specialPatches > 0) {
        true -> listOf(createSpecialPatch())
        else -> patches.take(3).toList()
    }

    fun advance(player: Player): Boolean {
        val otherPlayer = listOf(this.player1, player2).first { it != player }

        if (player.trackerPosition > otherPlayer.trackerPosition) {
            return false
        }

        player.advance(otherPlayer.trackerPosition - player.trackerPosition + 1)

        resetNextPlayer()

        return true
    }

    fun place(player: Player, patch: Patch, anchor: Position): Boolean {
        if (patch.buttonCost > player.buttons) {
            return false
        }

        val wasPlaced = player.place(patch, anchor)
        if (wasPlaced) {
            val distance = patches.take(3).indexOf(patch)
            patches.remove(patch)
            Collections.rotate(patches, distance)
        }

        resetNextPlayer()

        return wasPlaced
    }

    private fun resetNextPlayer() {
        val trackSorted = listOf(player1, player2)
            .filter { it.trackerPosition < 53 }
            .sortedBy { it.trackerPosition }

        val specialPatchPlayer = trackSorted.firstOrNull { it.specialPatches > 0 }

        nextPlayer = when (specialPatchPlayer) {
            null -> trackSorted.firstOrNull()
            else -> specialPatchPlayer
        }
    }
}
