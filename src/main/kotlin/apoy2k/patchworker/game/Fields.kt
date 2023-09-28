package apoy2k.patchworker.game

typealias Fields = List<List<Boolean>>

fun Fields.checksum(): Long {
    var result = 0L
    for (row in 0..<8) {
        for (col in 0..<8) {
            val field = this.getOrNull(row)?.getOrNull(col)
            if (field != null) {
                result = result.shl(1)
                result += when (field) {
                    true -> 1
                    else -> 0
                }
            }
        }
    }
    return result
}

fun Fields.rotate(): Fields {
    val numRows = this.size
    val numCols = this[0].size

    val newFields = List(numCols) { MutableList(numRows) { false } }
    for (row in 0..<numRows) {
        for (col in 0..<numCols) {
            newFields[col][numRows - 1 - row] = this[row][col]
        }
    }

    return newFields
}

fun Fields.flip(): Fields {
    val numRows = this.size
    val numCols = this[0].size

    val newFields = List(numRows) { MutableList(numCols) { false } }
    for (row in 0..<numRows) {
        for (col in 0..<numCols) {
            newFields[row][col] = this[row][numCols - 1 - col]
        }
    }

    return newFields
}
