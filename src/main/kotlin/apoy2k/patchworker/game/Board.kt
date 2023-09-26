package apoy2k.patchworker.game

fun Fields.stateChecksum() = StringBuilder()
    .append("") // TODO How to build a checksum that is rotation/mirroring independent?
    .toString()

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
