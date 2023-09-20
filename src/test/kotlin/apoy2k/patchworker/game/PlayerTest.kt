package apoy2k.patchworker.game

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

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
}
