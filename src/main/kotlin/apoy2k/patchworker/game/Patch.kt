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
        fields = fields.rotate()
    }

    fun flip() {
        fields = fields.flip()
    }

    fun copy() = Patch(
        id,
        buttonCost,
        timeCost,
        buttonIncome,
        fields.map { it.toMutableList() }.toMutableList()
    )
}
