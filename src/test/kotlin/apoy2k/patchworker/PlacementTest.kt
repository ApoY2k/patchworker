package apoy2k.patchworker

import apoy2k.patchworker.game.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PlacementTest {

    @ParameterizedTest
    @MethodSource("applyPatchData")
    fun `apply patch`(inputBoard: Fields, patch: Fields, anchor: Position, expectedBoard: Fields) {
        val newBoard = place(inputBoard, patch, anchor)
        assertEquals(expectedBoard, newBoard)
    }

    @Test
    fun `apply patch over filled space`() {
        assertFailsWith(InvalidPlacementException::class) {
            place(
                createPatchFields(
                    -2, 0, 0, -1,
                    -2, -2, -2, -1,
                    -2, -2, -2
                ),
                createPatchFields(
                    2, -1,
                    2,
                ),
                Position(0, 1)
            )
        }
    }

    @Test
    fun `apply patch outside boundaries`() {
        assertFailsWith(InvalidPlacementException::class) {
            place(
                createPatchFields(
                    -2, 0, 0, -1,
                    -2, -2, -2, -1,
                    -2, -2, -2
                ),
                createPatchFields(
                    2, -1,
                    2,
                ),
                Position(0, 3)
            )
        }
    }

    @Test
    fun `apply patch that leaks outside boundaries`() {
        assertFailsWith(InvalidPlacementException::class) {
            place(
                createPatchFields(
                    -2, 0, 0, -1,
                    -2, -2, -2, -1,
                    -2, -2, -2
                ),
                createPatchFields(
                    2, 2, 2, 2
                ),
                Position(0, 1)
            )
        }
    }

    companion object {

        @JvmStatic
        private fun applyPatchData() = Stream.of(
            Arguments.of(
                createPatchFields(
                    -2, -2, -2, -1,
                    -2, -2, -2, -1,
                    -2, -2, -2, -1,
                    -2, -2, -2
                ),
                createPatchFields(
                    2, 2, -1,
                    2, 0
                ),
                Position(0, 0),
                createPatchFields(
                    0, 0, -2, -1,
                    0, -2, -2, -1,
                    -2, -2, -2, -1,
                    -2, -2, -2
                )
            ),
            Arguments.of(
                createPatchFields(
                    -2, -2, -2, -1,
                    -2, -2, -2, -1,
                    -2, -2, -2, -1,
                    -2, -2, -2
                ),
                createPatchFields(
                    2, 2, 3, -1,
                    2, 2, 0, -1,
                    0, 3, 0
                ),
                Position(1, 0),
                createPatchFields(
                    -2, -2, -2, -1,
                    0, 0, 1, -1,
                    0, 0, -2, -1,
                    -2, 1, -2
                )
            )
        )
    }
}
