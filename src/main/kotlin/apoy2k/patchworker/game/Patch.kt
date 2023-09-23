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
    private val name = "Patch($buttonCost|$timeCost|$buttonIncome|${fields.flatten().size})"

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

    fun copy() = Patch(
        buttonCost,
        timeCost,
        buttonIncome,
        fields.map { it.toMutableList() }.toMutableList()
    )

    override fun toString() = name

    override fun equals(other: Any?): Boolean {
        if (other !is Patch) {
            return false
        }

        return this.buttonCost == other.buttonCost
                && this.timeCost == other.timeCost
                && this.buttonIncome == other.buttonIncome
                && this.fields == other.fields
    }

    override fun hashCode(): Int {
        var result = buttonCost
        result = 31 * result + timeCost
        result = 31 * result + buttonIncome
        result = 31 * result + name.hashCode()
        result = 31 * result + fields.hashCode()
        return result
    }
}
