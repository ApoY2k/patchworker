package apoy2k.patchworker.game

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GameTest {

    @Test
    fun `patch options`() {
        val patch = createPatch_2X1_I()
        val game = Game(mutableListOf(patch))
        val player = game.nextPlayer!!
        game.place(player, patch, Position(0, 0))
        assertEquals(-154, scorePlayer(player))
    }

    @Test
    fun `placing removes rotated patch`() {
        val patches = listOf(createPatch_2X1_I(), createPatch_2X2_L(), createPatch_2X2_L2())
        val game = Game(patches.toMutableList())
        val player = game.nextPlayer!!

        patches[1].flip()
        game.place(player, patches[1], Position(0, 0))
        assertEquals(2, game.getRemainingPatches())
        assertEquals(mutableListOf(patches[2], patches[0]), game.getPatchOptions(player))
    }

    @Test
    fun `earning special tile brings extra turn to place it`() {
        val patches = listOf(Patch(0, 30, 0, createPatchFields(X)))
        val game = Game(patches.toMutableList())
        val player = game.nextPlayer!!

        game.place(player, patches[0], Position(0, 0))

        val nextPlayer = game.nextPlayer!!
        val patchOptions = game.getPatchOptions(nextPlayer)

        assertEquals(1, nextPlayer.specialPatches)
        assertEquals(createSpecialPatch(), patchOptions[0])
        assertEquals(player, nextPlayer)
    }
}
