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

    var buttons: Int = buttons
        private set

    var specialPatches: Int = specialPatches
        private set

    var actionsTaken: Int = actionsTaken
        private set

    var buttonMultiplier: Int = buttonMultiplier
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

        val placeResult = tryPlace(board, patch.fields, anchor)
        if (placeResult.first) {
            board = placeResult.second

            buttons -= patch.buttonCost
            buttonMultiplier += patch.buttonIncome

            if (patch.id == SPECIAL_PATCH_ID) {
                specialPatches -= 1
            } else {
                val income = calculateIncome(trackerPosition, patch.timeCost)
                trackerPosition += patch.timeCost
                buttons += buttonMultiplier * income.first
                specialPatches += income.second
            }

            actionsTaken++
        }

        return placeResult.first
    }

    fun copy() = Player(
        board.map { row -> row.toMutableList() }.toMutableList(),
        trackerPosition,
        buttonMultiplier,
        buttons,
        specialPatches,
        actionsTaken
    )

    fun checksum() = StringBuilder()
        .append(trackerPosition).append(",")
        .append(buttonMultiplier).append(",")
        .append(buttons).append(",")
        .append(specialPatches).append(",")
        .append(actionsTaken).append(",")
        .append(board.checksum())
        .toString()
}
