package apoy2k.patchworker.game

class Game(
    private val patches: MutableList<Patch> = generatePatches()
) {
    val player = Player()
    val enemy = Player()

    var nextPlayer: Player? = player
        private set

    private var patchMarkerIndex: Int = 0

    fun getRemainingPatches() = patches.size

    fun getPatchOptions() = when (player.specialPatches > 0) {
        true -> listOf(createSpecialPatch())
        else -> patches.subList(patchMarkerIndex, patchMarkerIndex + 2)
    }

    fun advance(player: Player): Boolean {
        val otherPlayer = listOf(this.player, enemy).first { it != player }

        if (player.trackerPosition > otherPlayer.trackerPosition) {
            return false
        }

        player.advance(otherPlayer.trackerPosition - player.trackerPosition + 1)

        findNextPlayer()

        return true
    }

    fun place(player: Player, patch: Patch, anchor: Position): Boolean {
        if (patch.buttonCost > player.buttons) {
            return false
        }

        val wasPlaced = player.place(patch, anchor)
        if (wasPlaced) {
            patchMarkerIndex = patches.indexOf(patch)
            patches.remove(patch)
        }

        findNextPlayer()

        return wasPlaced
    }

    private fun findNextPlayer() {
        val trackSorted = listOf(player, enemy)
            .filter { it.trackerPosition < 53 }
            .sortedBy { it.trackerPosition }

        val specialPatchPlayer = trackSorted.firstOrNull { it.specialPatches > 0 }

        nextPlayer = when (specialPatchPlayer) {
            null -> trackSorted.firstOrNull()
            else -> specialPatchPlayer
        }
    }
}
