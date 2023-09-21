package apoy2k.patchworker.game

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class PlayerTest {

    @Test
    fun `place patch mutate player`() {
        val player = Player()
        val patch = Patch(2, 2, 0, createPatchFields(X, X))
        player.place(patch, Position(0, 0))
        assertEquals(2, player.trackerPosition)
        assertEquals(3, player.buttons)
    }

    @Test
    fun `prevent placing too expensive patches`() {
        val player = Player()
        val patch = Patch(6, 2, 0, createPatchFields(X, X))
        assertFalse(player.place(patch, Position(0, 0)))
    }

    @Test
    fun `place patch mutate player an calculates incomes`() {
        val player = Player()
        val patch = Patch(2, 5, 1, createPatchFields(X, X))
        player.place(patch, Position(0, 0))
        assertEquals(5, player.trackerPosition)

        // 5 Buttons start - 2 Button cost + 1 button income on patch
        // -> X button end result after
        // tracker advancement
        assertEquals(4, player.buttons)
    }

    @Test
    fun `score player after placing patch`() {
        val player = Player()
        val patch = Patch(2, 5, 1, createPatchFields(X, X))
        player.place(patch, Position(0, 0))

        // -162 initial
        // +5 initial buttons
        // -2 button costs for patch
        // +4 for covered area from patch
        // +1 for button income
        // = -154
        assertEquals(-154, scorePlayer(player))
    }

    @Test
    fun `player advances and gets income`() {
        val player = Player()
        player.advance(20)

        // -162 initial
        // +5 initial buttons
        // +20 from steps
        // = -137
        assertEquals(-137, scorePlayer(player))
    }


    @Test
    fun `player advances and gets income when already having button income`() {
        val player = Player()
        player.advance(20)
        player.place(Patch(5, 20, 5, createPatchFields(X)), Position(0, 0))

        // -162 initial
        // +5 initial buttons
        // -5 button cost
        // +20 from steps
        // +2 from covered area from patch
        // +15 (3 * 5 button income)
        // = -125
        assertEquals(-125, scorePlayer(player))
    }
}
