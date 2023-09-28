package apoy2k.patchworker.game

typealias Fields = List<List<Boolean>>

const val O = false
const val X = true

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

fun createBoard() = createPatchFields(
    O, O, O, O, O, O, O, O, O, null,
    O, O, O, O, O, O, O, O, O, null,
    O, O, O, O, O, O, O, O, O, null,
    O, O, O, O, O, O, O, O, O, null,
    O, O, O, O, O, O, O, O, O, null,
    O, O, O, O, O, O, O, O, O, null,
    O, O, O, O, O, O, O, O, O, null,
    O, O, O, O, O, O, O, O, O, null,
    O, O, O, O, O, O, O, O, O
)
