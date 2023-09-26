package apoy2k.patchworker.game

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class GameTest {

    @Test
    fun `patch options`() {
        val patch = createPatch_2X1_I()
        val game = Game(mutableListOf(patch))
        game.place(patch, Position(0, 0))
        assertEquals(-154, scorePlayer(game.player1))
    }

    @Test
    fun `placing removes rotated patch`() {
        val patches = listOf(createPatch_2X1_I(), createPatch_2X2_L(), createPatch_2X2_L2())
        val game = Game(patches.toMutableList())

        patches[1].flip()
        game.place(patches[1], Position(0, 0))
        assertEquals(2, game.getRemainingPatches())
        assertEquals(mutableListOf(patches[2], patches[0]), game.getPatchOptions())
    }

    @Test
    fun `earning special tile brings extra turn to place it`() {
        val patches = listOf(Patch(0, 30, 0, createPatchFields(X)))
        val game = Game(patches.toMutableList())

        game.place(patches[0], Position(0, 0))

        val nextPlayer = game.nextPlayer!!
        val patchOptions = game.getPatchOptions()

        assertEquals(1, nextPlayer.specialPatches)
        assertEquals(createSpecialPatch(), patchOptions[0])
        assertEquals(game.player1, nextPlayer)
    }

    @Test
    fun `state checksum of equivalent games match but games are not equal`() {
        val patches1 = listOf(Patch(0, 30, 0, createPatchFields(X)))
        val game1 = Game(patches1.toMutableList())

        game1.place(patches1[0], Position(0, 0))

        val patches2 = listOf(Patch(0, 30, 0, createPatchFields(X)))
        val game2 = Game(patches2.toMutableList())

        game2.place(patches2[0], Position(0, 0))

        assertNotEquals(game1, game2)
        assertEquals(game1.stateChecksum(), game2.stateChecksum())
    }

    @Test
    fun `placing a copied rotated patch removes it from available patch options`() {
        val patches = listOf(Patch(1, 1, 1, createPatchFields(X, O, null, X, X)))
        val game = Game(patches.toMutableList())
        val copiedPatch = patches[0].copy()
        copiedPatch.rotate()
        game.place(copiedPatch, Position(0, 0))
        assertEquals(0, game.getRemainingPatches())
    }
}
