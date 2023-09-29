package apoy2k.patchworker.game

import java.math.BigInteger

typealias Fields = List<FieldRow>
typealias FieldRow = List<Boolean>

const val O = false
const val X = true

fun Fields.checksum(): String {
    var result = BigInteger.ZERO
    for (field in this.flatten()) {
        result = result.shl(1)
        result += when (field) {
            true -> BigInteger.ONE
            else -> BigInteger.ZERO
        }
    }
    return result.toString()
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
