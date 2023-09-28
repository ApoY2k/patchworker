@file:Suppress("FunctionName")

package apoy2k.patchworker.game

const val O = false
const val X = true
const val SPECIAL_PATCH_ID = "special"

/**
 * Create a 2d field matrix from a set of input values
 * where null denotes a "line break", to start a start new row
 */
fun createPatchFields(vararg values: Boolean?): Fields {
    val result = mutableListOf<List<Boolean>>()
    val currentRow = mutableListOf<Boolean>()

    values.forEach {
        if (it == null) {
            result.add(currentRow.toList())
            currentRow.clear()
        } else {
            currentRow.add(it)
        }
    }

    result.add(currentRow)

    return result
}

fun createSpecialPatch() = Patch(SPECIAL_PATCH_ID, 0, 0, 0, createPatchFields(X))

fun createPatch_2X1_I() = Patch("2X1_I", 1, 2, 0, createPatchFields(X, X))

fun createPatch_2X2_L() = Patch(
    "2X2_L", 1, 3, 0, createPatchFields(
        X, X, null,
        O, X
    )
)

fun createPatch_2X2_L2() = Patch(
    "2X2_L2", 3, 1, 0, createPatchFields(
        X, X, null,
        O, X
    )
)

fun createPatch_2X2_O() = Patch(
    "2X2_O", 6, 5, 2, createPatchFields(
        X, X, null,
        X, X
    )
)

fun createPatch_2X3_P() = Patch(
    "2X3_P", 2, 2, 0, createPatchFields(
        X, O, null,
        X, X, null,
        X, X
    )
)

fun createPatch_3X1_I() = Patch("3X1_I", 2, 2, 0, createPatchFields(X, X, X))

fun createPatch_3X2_L() = Patch(
    "3X2_L", 4, 2, 1, createPatchFields(
        X, X, X, null,
        O, O, X
    )
)

fun createPatch_3X2_L2() = Patch(
    "3X2_L2", 4, 6, 2, createPatchFields(
        X, X, X, null,
        X, O, O
    )
)

fun createPatch_3X2_T() = Patch(
    "3X2_T", 2, 2, 0, createPatchFields(
        X, X, X, null,
        O, X, O
    )
)

fun createPatch_3X2_U() = Patch(
    "3X2_U", 1, 2, 0, createPatchFields(
        X, O, X, null,
        X, X, X
    )
)

fun createPatch_3X2_Z() = Patch(
    "3X2_Z", 7, 6, 3, createPatchFields(
        X, X, O, null,
        O, X, X
    )
)

fun createPatch_3X2_Z2() = Patch(
    "3X2_Z2", 3, 2, 1, createPatchFields(
        X, X, O, null,
        O, X, X
    )
)

fun createPatch_3X3_H() = Patch(
    "3X3_H", 2, 3, 0, createPatchFields(
        X, X, X, null,
        O, X, O, null,
        X, X, X
    )
)

fun createPatch_3X3_Q() = Patch(
    "3X3_Q", 8, 6, 3, createPatchFields(
        X, X, O, null,
        O, X, X, null,
        O, X, X
    )
)

fun createPatch_3X3_T() = Patch(
    "3X3_T", 5, 5, 2, createPatchFields(
        X, X, X, null,
        O, X, O, null,
        O, X, O
    )
)

fun createPatch_3X3_W() = Patch(
    "3X3_W", 10, 4, 3, createPatchFields(
        X, O, O, null,
        X, X, O, null,
        O, X, X
    )
)

fun createPatch_3X3_X() = Patch(
    "3X3_X", 5, 4, 2, createPatchFields(
        O, X, O, null,
        X, X, X, null,
        O, X, O
    )
)

fun createPatch_3X3_Y() = Patch(
    "3X3_Y", 3, 6, 2, createPatchFields(
        O, X, O, null,
        X, X, X, null,
        X, O, X
    )
)

fun createPatch_3X4_T() = Patch(
    "3X4_T", 7, 2, 2, createPatchFields(
        X, X, X, null,
        O, X, O, null,
        O, X, O, null,
        O, X, O
    )
)

fun createPatch_4X1_I() = Patch("4X1_I", 3, 3, 1, createPatchFields(X, X, X, X))

fun createPatch_4X2_L() = Patch(
    "4X2_L", 10, 3, 2, createPatchFields(
        X, X, X, X, null,
        O, O, O, X
    )
)

fun createPatch_4X2_P() = Patch(
    "4X2_P", 10, 5, 3, createPatchFields(
        X, X, X, X, null,
        O, O, X, X
    )
)

fun createPatch_4X2_R() = Patch(
    "4X2_R", 3, 4, 1, createPatchFields(
        O, X, O, O, null,
        X, X, X, X
    )
)

fun createPatch_4X2_T() = Patch(
    "4X2_T", 7, 4, 2, createPatchFields(
        O, X, X, O, null,
        X, X, X, X
    )
)

fun createPatch_4X2_U() = Patch(
    "4X2_U", 1, 5, 1, createPatchFields(
        X, X, X, X, null,
        X, O, O, X
    )
)

fun createPatch_4X2_Z() = Patch(
    "4X2_Z", 4, 2, 0, createPatchFields(
        X, X, X, O, null,
        O, X, X, X
    )
)

fun createPatch_4X2_Z2() = Patch(
    "4X2_Z2", 2, 3, 1, createPatchFields(
        X, X, O, O, null,
        O, X, X, X
    )
)

fun createPatch_4X3_K() = Patch(
    "4X3_K", 2, 1, 0, createPatchFields(
        O, X, O, O, null,
        X, X, X, X, null,
        O, O, X, O
    )
)

fun createPatch_4X3_O() = Patch(
    "4X3_O", 5, 3, 1, createPatchFields(
        O, X, X, O, null,
        X, X, X, X, null,
        O, X, X, O
    )
)

fun createPatch_4X3_X() = Patch(
    "4X3_X", 0, 3, 1, createPatchFields(
        O, X, O, O, null,
        X, X, X, X, null,
        O, X, O, O
    )
)

fun createPatch_4X3_Z() = Patch(
    "4X3_Z", 1, 2, 0, createPatchFields(
        X, O, O, O, null,
        X, X, X, X, null,
        O, O, O, X
    )
)

fun createPatch_5X1_I() = Patch("5X1_I", 7, 1, 1, createPatchFields(X, X, X, X, X))

fun createPatch_5X3_X() = Patch(
    "5X3_X", 1, 4, 1, createPatchFields(
        O, O, X, O, O, null,
        X, X, X, X, X, null,
        O, O, X, O, O
    )
)

fun generatePatches() = mutableListOf(
    createPatch_2X1_I(),
    createPatch_2X2_L(),
    createPatch_2X2_L2(),
    createPatch_2X2_O(),
    createPatch_2X3_P(),
    createPatch_3X1_I(),
    createPatch_3X2_L(),
    createPatch_3X2_L2(),
    createPatch_3X2_T(),
    createPatch_3X2_U(),
    createPatch_3X2_Z(),
    createPatch_3X2_Z2(),
    createPatch_3X3_H(),
    createPatch_3X3_Q(),
    createPatch_3X3_T(),
    createPatch_3X3_W(),
    createPatch_3X3_X(),
    createPatch_3X3_Y(),
    createPatch_3X4_T(),
    createPatch_4X1_I(),
    createPatch_4X2_L(),
    createPatch_4X2_P(),
    createPatch_4X2_R(),
    createPatch_4X2_T(),
    createPatch_4X2_U(),
    createPatch_4X2_Z(),
    createPatch_4X2_Z2(),
    createPatch_4X3_K(),
    createPatch_4X3_O(),
    createPatch_4X3_X(),
    createPatch_4X3_Z(),
    createPatch_5X1_I(),
    createPatch_5X3_X()
)
