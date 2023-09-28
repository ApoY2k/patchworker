package apoy2k.patchworker.game

typealias Fields = List<FieldRow>
typealias FieldRow = List<Boolean>

const val O = false
const val X = true

fun Fields.checksum(): String {
    val result = StringBuilder()
    val list = this.flatten()
    result.append(list.checksum(0)).append(":")
    result.append(list.checksum(3)).append(":")
    result.append(list.checksum(6))
    return result.toString()
}

private fun FieldRow.checksum(start: Int): Int {
    var result = 0
    for (idx in start..start + 2) {
        result = result.shl(1)
        result += when (this[idx]) {
            true -> 1
            else -> 0
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
