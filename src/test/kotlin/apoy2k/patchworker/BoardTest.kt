package apoy2k.patchworker

import apoy2k.patchworker.game.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BoardTest {

    @Test
    fun `score board`() {
        val board = createBoard()
        assertEquals(-162, scoreBoard(board, 9))
    }

    @Test
    fun `place patch and check new score`() {
        val board = createBoard()
        place(board, createPatchFields(2, 2), Position(0, 0))
        assertEquals(-158, scoreBoard(board, 9))
    }
}
