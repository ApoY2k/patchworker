package apoy2k.patchworker.game

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GameTest {

    @Test
    fun `patch options`() {
        val patch = createPatch_2X1_I()
        val game = Game(mutableListOf(patch))
        val nextPlayer = game.getNextPlayer()
        game.place(nextPlayer, patch, Position(0, 0))
        assertEquals(-154, scorePlayer(nextPlayer))
    }

    @Test
    fun `placing removes rotated patch`() {
        val patches = listOf(createPatch_2X1_I(), createPatch_2X2_L(), createPatch_2X2_L2())
        val game = Game(patches.toMutableList())
        val nexPlayer = game.getNextPlayer()

        patches[1].flip()
        game.place(nexPlayer, patches[1], Position(0, 0))
        assertEquals(2, game.getRemainingPatches())
        assertEquals(mutableListOf(patches[0], patches[2]), game.getPatchOptions())
    }
}
