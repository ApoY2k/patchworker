package apoy2k.patchworker.game

typealias Fields = List<FieldRow>
typealias FieldRow = List<Boolean>

const val O = false
const val X = true

fun Fields.checksum(): String {
    val result = StringBuilder()
    val list = this.flatten()
    result.append(list.checksum(0)).append(":")
    result.append(list.checksum(27)).append(":")
    result.append(list.checksum(54))
    return result.toString()
}

private fun FieldRow.checksum(start: Int): Int {
    var result = 0
    for (idx in start..start + 26) {
        result = result.shl(1)
        result += when (this[idx]) {
            true -> 1
            else -> 0
        }
    }
    return result
}

fun createBoard() = createPatchFields(
    O, O, O, O, O, O, O, O, O, null, //  0- 8
    O, O, O, O, O, O, O, O, O, null, //  9-17
    O, O, O, O, O, O, O, O, O, null, // 18-26
    O, O, O, O, O, O, O, O, O, null, // 27-35
    O, O, O, O, O, O, O, O, O, null, // 36-44
    O, O, O, O, O, O, O, O, O, null, // 45-53
    O, O, O, O, O, O, O, O, O, null, // 54-62
    O, O, O, O, O, O, O, O, O, null, // 63-71
    O, O, O, O, O, O, O, O, O        // 72-80
)
