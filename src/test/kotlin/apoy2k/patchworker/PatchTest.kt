package apoy2k.patchworker

import apoy2k.patchworker.game.Patch
import apoy2k.patchworker.game.createPatchFields
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class PatchTest {

    @ParameterizedTest
    @MethodSource("rotatePatchData")
    fun `rotate patch`(initialPatch: List<List<Int>>, expectedRotated: List<List<Int>>) {
        val patch = Patch(0, 0, initialPatch)
        patch.rotate()
        assertEquals(expectedRotated, patch.fields)
    }

    @ParameterizedTest
    @MethodSource("flipPatchData")
    fun `flip patch`(initialPatch: List<List<Int>>, expectedFlipped: List<List<Int>>) {
        val patch = Patch(0, 0, initialPatch)
        patch.flip()
        assertEquals(expectedFlipped, patch.fields)
    }

    companion object {
        @JvmStatic
        private fun rotatePatchData() = Stream.of(
            Arguments.of(createPatchFields(2, 2), createPatchFields(2, -1, 2)),
            Arguments.of(
                createPatchFields(
                    2, 0, -1,
                    2, 2
                ), createPatchFields(
                    2, 2, -1,
                    2, 0
                )
            ),
            Arguments.of(
                createPatchFields(
                    2, 3, 3, -1,
                    2, 2, 0, -1,
                    0, 2, 0
                ), createPatchFields(
                    0, 2, 2, -1,
                    2, 2, 3, -1,
                    0, 0, 3
                )
            ),
            Arguments.of(
                createPatchFields(
                    2, 3, 3, -1,
                    2, 2, 0,
                ), createPatchFields(
                    2, 2, -1,
                    2, 3, -1,
                    0, 3
                )
            ),
        )

        @JvmStatic
        private fun flipPatchData() = Stream.of(
            Arguments.of(createPatchFields(2, 0), createPatchFields(0, 2)),
            Arguments.of(createPatchFields(2, 2), createPatchFields(2, 2)),
            Arguments.of(
                createPatchFields(
                    2, 0, -1,
                    2, 2
                ), createPatchFields(
                    0, 2, -1,
                    2, 2
                )
            ),
            Arguments.of(
                createPatchFields(
                    2, 3, 3, -1,
                    2, 2, 0, -1,
                    0, 2, 0
                ), createPatchFields(
                    3, 3, 2, -1,
                    0, 2, 2, -1,
                    0, 2, 0
                )
            ),
            Arguments.of(
                createPatchFields(
                    2, 3, 3, -1,
                    2, 2, 0,
                ), createPatchFields(
                    3, 3, 2, -1,
                    0, 2, 2
                )
            ),
        )
    }
}
