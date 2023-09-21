package apoy2k.patchworker.game

typealias Fields = List<List<Boolean>>

const val O = false
const val X = true

data class Patch(
    val buttonCost: Int,
    val timeCost: Int,
    val buttonIncome: Int,
    var fields: Fields
) {
    private val name = "Patch($buttonCost|$timeCost|$buttonIncome|${fields.flatten().size})"

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

    override fun toString() = name
}
