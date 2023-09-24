package apoy2k.patchworker.game

typealias Position = Pair<Int, Int>

class InvalidPlacementException : Exception()

fun tryPlace(board: Fields, patch: Fields, anchor: Position) =
    try {
        val newBoard = place(board, patch, anchor)
        Pair(true, newBoard)
    } catch (ip: InvalidPlacementException) {
        Pair(false, board)
    }

private fun place(board: Fields, patch: Fields, anchor: Position): Fields {
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
        colFields.mapIndexed newField@{ col, field ->
            // If the current field is not within the range of the patch, just return it as is
            val point = Position(row, col)
            if (!(point.first in applyRowRange && point.second in applyColRange)) {
                return@newField field
            }

            // If the field is already filled, nothing can be placed on it
            if (field) {
                throw InvalidPlacementException()
            }

            return@newField patch[row - anchor.first][col - anchor.second]
        }
    }
}
