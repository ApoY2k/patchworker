package apoy2k.patchworker.game

typealias Fields = List<List<Boolean>>

const val O = false
const val X = true

data class Patch(
    val timeCost: Int,
    val buttonCost: Int,
    val buttonIncome: Int,
    var fields: Fields
) {
    fun rotate() {
        val numRows = fields.size
        val numCols = fields[0].size

        val newFields = List(numCols) { MutableList(numRows) { false } }
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                newFields[col][numRows - 1 - row] = fields[row][col]
            }
        }

        fields = newFields
    }

    fun flip() {
        val numRows = fields.size
        val numCols = fields[0].size

        val newFields = List(numRows) { MutableList(numCols) { false } }
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                newFields[row][col] = fields[row][numCols - 1 - col]
            }
        }

        fields = newFields
    }
}

/**
 * Create a 2d field matrix from a set of input values
 * where null denotes a "line break", to start a start new row
 */
fun createPatchFields(vararg values: Boolean?): Fields {
    val result = mutableListOf<List<Boolean>>()
    val currentRow = mutableListOf<Boolean>()

    values.forEach {
        if (it == null) {
            result.add(currentRow.toList())
            currentRow.clear()
        } else {
            currentRow.add(it)
        }
    }

    result.add(currentRow)

    return result
}

val PATCH_2X1 = Patch(2, 1, 0, createPatchFields(
    X, X
))
