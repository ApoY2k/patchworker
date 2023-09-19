package apoy2k.patchworker.game

class Game {
    private val player = Player()
    private val enemy = Player()

    private val patches = mutableListOf(
        PATCH_2X1
    ).also { it.shuffle() }

    fun getPatchOptions() = patches.subList(0, 3)

    fun nextPlayer(): Player {
        val trackSorted = listOf(player, enemy)
            .sortedBy { it.trackerPosition }

        val specialTilePlayer = trackSorted.firstOrNull { it.specialTiles > 0 }

        return when (specialTilePlayer) {
            null -> trackSorted.first()
            else -> specialTilePlayer
        }
    }

    fun place(player: Player, patch: Patch, anchor: Position) {
        player.place(patch, anchor)
        patches.remove(patch)
    }
}
