package apoy2k.patchworker.game

import kotlin.math.min

class Player {
    private val id = Thread.currentThread().id
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

    fun copy() = Player().also {
        it.board = board.map { row -> row.toMutableList() }.toMutableList()
        it.trackerPosition = trackerPosition
        it.specialPatches = specialPatches
        it.buttonMultiplier = buttonMultiplier
        it.buttons = buttons
    }

    override fun toString() = "Player#$id($trackerPosition|$buttons)"
}
