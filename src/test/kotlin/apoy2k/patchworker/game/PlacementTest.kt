package apoy2k.patchworker.game

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
                    O, X, X, null,
                    O, O, O, null,
                    O, O, O
                ),
                createPatchFields(
                    X, null,
                    X,
                ),
                Position(0, 2)
            )
        }
    }

    @Test
    fun `apply patch outside boundaries`() {
        assertFailsWith(InvalidPlacementException::class) {
            place(
                createPatchFields(
                    O, X, X, null,
                    O, O, O, null,
                    O, O, O
                ),
                createPatchFields(
                    X, null,
                    X,
                ),
                Position(2, 3)
            )
        }
    }

    @Test
    fun `apply patch that leaks outside boundaries`() {
        assertFailsWith(InvalidPlacementException::class) {
            place(
                createPatchFields(
                    O, X, X, null,
                    O, O, O, null,
                    O, O, O
                ),
                createPatchFields(
                    X, X, X, X
                ),
                Position(2, 1)
            )
        }
    }

    companion object {

        @JvmStatic
        private fun applyPatchData() = Stream.of(
            Arguments.of(
                createPatchFields(
                    O, O, O, null,
                    O, O, O, null,
                    O, O, O, null,
                    O, O, O
                ),
                createPatchFields(
                    X, X, null,
                    X, O
                ),
                Position(0, 0),
                createPatchFields(
                    X, X, O, null,
                    X, O, O, null,
                    O, O, O, null,
                    O, O, O
                )
            ),
            Arguments.of(
                createPatchFields(
                    O, O, O, null,
                    O, O, O, null,
                    O, O, O, null,
                    O, O, O
                ),
                createPatchFields(
                    X, X, X, null,
                    X, X, O, null,
                    O, X, O
                ),
                Position(1, 0),
                createPatchFields(
                    O, O, O, null,
                    X, X, X, null,
                    X, X, O, null,
                    O, X, O
                )
            )
        )
    }
}
