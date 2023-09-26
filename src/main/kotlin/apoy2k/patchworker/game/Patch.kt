package apoy2k.patchworker.game

typealias Fields = List<List<Boolean>>

const val O = false
const val X = true

class Patch(
    val buttonCost: Int,
    val timeCost: Int,
    val buttonIncome: Int,
    fields: Fields
) {
    var fields: Fields = fields
        private set

    fun rotate() {
        val numRows = fields.size
        val numCols = fields[0].size

        val newFields = List(numCols) { MutableList(numRows) { false } }
        for (row in 0..<numRows) {
            for (col in 0..<numCols) {
                newFields[col][numRows - 1 - row] = fields[row][col]
            }
        }

        fields = newFields
    }

    fun flip() {
        val numRows = fields.size
        val numCols = fields[0].size

        val newFields = List(numRows) { MutableList(numCols) { false } }
        for (row in 0..<numRows) {
            for (col in 0..<numCols) {
                newFields[row][col] = fields[row][numCols - 1 - col]
            }
        }

        fields = newFields
    }

    fun stateChecksum() = StringBuilder()
        .append(buttonCost)
        .append(timeCost)
        .append(buttonIncome)
        .append(fields.stateChecksum())
        .toString()

    fun copy() = Patch(
        buttonCost,
        timeCost,
        buttonIncome,
        fields.map { it.toMutableList() }.toMutableList()
    )
}
