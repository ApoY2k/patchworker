package apoy2k.patchworker.game

typealias Position = Pair<Int, Int>

class InvalidPlacementException : Exception()

fun place(board: Fields, patch: Fields, anchor: Position): Fields {
    val applyRowRange = anchor.first..<anchor.first + patch.size
    val applyColRange = anchor.second..<anchor.second + patch[0].size

    if (anchor.first >= board.size
        || anchor.second >= board[0].size
        || applyRowRange.last >= board.size
        || applyColRange.last >= board[0].size
    ) {
        throw InvalidPlacementException()
    }

    return board.mapIndexed { row, colFields ->
        colFields.mapIndexed { col, field ->
            val point = Position(row, col)
            if (!(point.first in applyRowRange && point.second in applyColRange)) {
                field
            } else {
                try {
                    val newField = field + patch[row - anchor.first][col - anchor.second]

                    // -2 is the default state of an empty board field
                    // patches without button increase this to 0
                    // patches with button increase this to 1
                    // anything above that means the board already has a patch on that position
                    if (newField > 1) {
                        throw InvalidPlacementException()
                    }

                    newField
                } catch (ex: IndexOutOfBoundsException) {
                    throw InvalidPlacementException()
                }
            }
        }
    }
}
