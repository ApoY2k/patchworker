package apoy2k.patchworker.game

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class PatchTest {

    @ParameterizedTest
    @MethodSource("rotatePatchData")
    fun `rotate patch`(initialPatch: List<List<Boolean>>, expectedRotated: List<List<Boolean>>) {
        val patch = Patch(0, 0, 0, initialPatch)
        patch.rotate()
        assertEquals(expectedRotated, patch.fields)
    }

    @ParameterizedTest
    @MethodSource("flipPatchData")
    fun `flip patch`(initialPatch: List<List<Boolean>>, expectedFlipped: List<List<Boolean>>) {
        val patch = Patch(0, 0, 0, initialPatch)
        patch.flip()
        assertEquals(expectedFlipped, patch.fields)
    }

    companion object {
        @JvmStatic
        private fun rotatePatchData() = Stream.of(
            Arguments.of(createPatchFields(X, X), createPatchFields(X, null, X)),
            Arguments.of(
                createPatchFields(
                    X, O, null,
                    X, X
                ), createPatchFields(
                    X, X, null,
                    X, O
                )
            ),
            Arguments.of(
                createPatchFields(
                    X, X, X, null,
                    X, X, O, null,
                    O, X, O
                ), createPatchFields(
                    O, X, X, null,
                    X, X, X, null,
                    O, O, X
                )
            ),
            Arguments.of(
                createPatchFields(
                    X, X, X, null,
                    X, X, O,
                ), createPatchFields(
                    X, X, null,
                    X, X, null,
                    O, X
                )
            ),
        )

        @JvmStatic
        private fun flipPatchData() = Stream.of(
            Arguments.of(createPatchFields(X, O), createPatchFields(O, X)),
            Arguments.of(createPatchFields(X, X), createPatchFields(X, X)),
            Arguments.of(
                createPatchFields(
                    X, O, null,
                    X, X
                ), createPatchFields(
                    O, X, null,
                    X, X
                )
            ),
            Arguments.of(
                createPatchFields(
                    X, X, X, null,
                    X, X, O, null,
                    O, X, O
                ), createPatchFields(
                    X, X, X, null,
                    O, X, X, null,
                    O, X, O
                )
            ),
            Arguments.of(
                createPatchFields(
                    X, X, X, null,
                    X, X, O,
                ), createPatchFields(
                    X, X, X, null,
                    O, X, X
                )
            ),
        )
    }
}
