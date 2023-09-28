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
            if (patch.id != SPECIAL_PATCH_ID) {
                val distance = patches.indexOfFirst { it.id == patch.id }
                patches.removeAt(distance)
                if (distance > 0) {
                    Collections.rotate(patches, distance)
                }
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

    fun checksum() = StringBuilder()
        .append(patches.joinToString(":") { it.id }).append(",")
        .append(player1.checksum()).append(",")
        .append(player2.checksum()).append(",")
        .append(isPlayer1Turn())
        .toString()

    private fun isPlayer1Turn() = nextPlayer == player1

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
