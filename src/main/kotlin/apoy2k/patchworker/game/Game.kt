package apoy2k.patchworker.game

import java.util.*

class NoActivePlayerException : Exception()

class Game(
    private val patches: MutableList<Patch> = generatePatches(),
    val player1: Player = Player(),
    val player2: Player = Player(),
    nextPlayer: Player? = null
) {
    var nextPlayer: Player? = nextPlayer
        private set

    init {
        resetNextPlayer()
    }

    fun getRemainingPatches() = patches.size

    fun getPatchOptions(): List<Patch> {
        val player = nextPlayer ?: throw NoActivePlayerException()

        return when (player.specialPatches > 0) {
            true -> listOf(createSpecialPatch())
            else -> patches.take(3).filter { it.buttonCost <= player.buttons }
        }
    }

    fun advance(): Boolean {
        val player = nextPlayer ?: throw NoActivePlayerException()

        val otherPlayer = listOf(player1, player2).first { it != player }

        if (player.trackerPosition > otherPlayer.trackerPosition) {
            return false
        }

        player.advance(otherPlayer.trackerPosition - player.trackerPosition + 1)

        resetNextPlayer()

        return true
    }

    fun place(patch: Patch, anchor: Position): Boolean {
        val player = nextPlayer ?: throw NoActivePlayerException()

        if (patch.buttonCost > player.buttons) {
            return false
        }

        val wasPlaced = player.place(patch, anchor)
        if (wasPlaced) {
            val distance = patches.take(3).indexOf(patch)
            patches.remove(patch)
            if (distance > 0) {
                Collections.rotate(patches, distance)
            }
            resetNextPlayer()
        }

        return wasPlaced
    }

    fun copy() = Game(
        patches.map { it.copy() }.toMutableList(),
        player1.copy(),
        player2.copy(),
        nextPlayer?.copy()
    )


    fun gameStateHash(): Int {
        // TODO built a unique identifier for the game state
        // to use it as key for the score map
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
