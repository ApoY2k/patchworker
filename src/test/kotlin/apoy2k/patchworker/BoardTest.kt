package apoy2k.patchworker

import apoy2k.patchworker.game.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BoardTest {

    @Test
    fun `score board`() {
        val board = createBoard()
        assertEquals(-162, scoreBoard(board))
    }

    @Test
    fun `place patch and check new score`() {
        val board = place(createBoard(), createPatchFields(X, X), Position(0, 0))
        assertEquals(-158, scoreBoard(board))
    }
}
