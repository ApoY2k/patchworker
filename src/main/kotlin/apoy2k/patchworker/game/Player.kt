package apoy2k.patchworker.game

import kotlin.math.min

class Player(
    board: Fields = createBoard(),
    trackerPosition: Int = 0,
    buttonMultiplier: Int = 0,
    buttons: Int = 5,
    specialPatches: Int = 0,
    actionsTaken: Int = 0
) {
    var board: Fields = board
        private set

    var trackerPosition: Int = trackerPosition
        private set

    var buttonMultiplier: Int = buttonMultiplier
        private set

    var buttons: Int = buttons
        private set

    var specialPatches: Int = specialPatches
        private set

    var actionsTaken: Int = actionsTaken
        private set

    fun advance(steps: Int) {
        val actualSteps = min(53 - trackerPosition, steps)

        trackerPosition += actualSteps
        val income = calculateIncome(trackerPosition, actualSteps)
        buttons += actualSteps + buttonMultiplier * income.first
        specialPatches += income.second

        actionsTaken++
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

            actionsTaken++

            true
        } catch (ip: InvalidPlacementException) {
            false
        }
    }

    fun copy() = Player(
        board.map { row -> row.toMutableList() }.toMutableList(),
        trackerPosition,
        buttonMultiplier,
        buttons,
        specialPatches,
        actionsTaken
    )
}
