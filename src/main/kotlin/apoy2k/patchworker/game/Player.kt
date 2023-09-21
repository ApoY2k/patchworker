package apoy2k.patchworker.game

class Player {
    private var buttonMultiplier = 0

    var board = createBoard()
        private set

    var trackerPosition = 0
        private set

    var buttons = 5
        private set

    var specialPatches = 0
        private set

    fun advance(steps: Int) {
        trackerPosition += steps
        buttons += steps
    }

    fun place(patch: Patch, anchor: Position): Boolean {
        if (patch.buttonCost > buttons) {
            return false
        }

        return try {
            buttons -= patch.buttonCost
            buttonMultiplier += patch.buttonIncome
            board = place(board, patch.fields, anchor)

            val income = calculateIncome(trackerPosition, patch.timeCost)

            trackerPosition += patch.timeCost
            buttons += buttonMultiplier * income.first
            specialPatches += income.second

            true
        } catch (ip: InvalidPlacementException) {
            false
        }
    }

    override fun toString() = "Player@${hashCode()} at position $trackerPosition with $buttons buttons"
}
