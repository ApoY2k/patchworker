package apoy2k.patchworker.game

import kotlin.math.min

class Player(
    val name: String = "player"
) {
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
        val actualSteps = min(53 - trackerPosition, steps)
        println("$this advances $actualSteps steps")

        trackerPosition += actualSteps
        val income = calculateIncome(trackerPosition, actualSteps)
        buttons += actualSteps + buttonMultiplier * income.first
        specialPatches += income.second
    }

    fun place(patch: Patch, anchor: Position): Boolean {
        if (patch.buttonCost > buttons) {
            return false
        }

        return try {
            board = tryPlace(board, patch.fields, anchor)
            println("$this has placed $patch")

            buttons -= patch.buttonCost
            buttonMultiplier += patch.buttonIncome

            if (patch.timeCost == 0) {
                specialPatches -= 1
            } else {
                val income = calculateIncome(trackerPosition, patch.timeCost)
                trackerPosition += patch.timeCost
                buttons += buttonMultiplier * income.first
                specialPatches += income.second
            }

            true
        } catch (ip: InvalidPlacementException) {
            false
        }
    }

    override fun toString() = "Player($name|$trackerPosition|$buttons)"
}
