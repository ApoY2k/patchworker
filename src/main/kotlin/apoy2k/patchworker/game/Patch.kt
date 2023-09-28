package apoy2k.patchworker.game

class Patch(
    val id: String,
    val buttonCost: Int,
    val timeCost: Int,
    val buttonIncome: Int,
    fields: Fields
) {
    var fields: Fields = fields
        private set

    fun rotate() {
        fields = run {
            val numRows = fields.size
            val numCols = fields[0].size
            val newFields = List(numCols) { MutableList(numRows) { false } }
            for (row in 0..<numRows) {
                for (col in 0..<numCols) {
                    newFields[col][numRows - 1 - row] = fields[row][col]
                }
            }
            newFields
        }
    }

    fun flip() {
        fields = run {
            val numRows = fields.size
            val numCols = fields[0].size
            val newFields = List(numRows) { MutableList(numCols) { false } }
            for (row in 0..<numRows) {
                for (col in 0..<numCols) {
                    newFields[row][col] = fields[row][numCols - 1 - col]
                }
            }
            newFields
        }
    }

    fun copy() = Patch(
        id,
        buttonCost,
        timeCost,
        buttonIncome,
        fields.map { it.toMutableList() }.toMutableList()
    )
}
