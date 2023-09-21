package apoy2k.patchworker.game

class Game(
    private val patches: MutableList<Patch> = generatePatches()
) {
    private val player = Player()
    private val enemy = Player()

    fun getPatchOptions(player: Player) = when (player.specialPatches > 0) {
        true -> listOf(createSpecialPatch())
        else -> patches.take(3).toList()
    }

    fun getRemainingPatches() = patches.size

    fun getNextPlayer(): Player {
        val trackSorted = listOf(player, enemy)
            .sortedBy { it.trackerPosition }

        val specialTilePlayer = trackSorted.firstOrNull { it.specialPatches > 0 }

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
