package apoy2k.patchworker

import apoy2k.patchworker.game.Patch
import apoy2k.patchworker.game.Player
import apoy2k.patchworker.game.Position
import apoy2k.patchworker.game.createPatchFields
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PlayerTest {

    @Test
    fun `place patch mutate player`() {
        val player = Player()
        val patch = Patch(2, 2, createPatchFields(2, 2))
        player.place(patch, Position(0, 0))
        assertEquals(2, player.trackerPosition)
        assertEquals(3, player.buttons)
    }

    @Test
    fun `place patch mutate player an calculates incomes`() {
        val player = Player()
        val patch = Patch(5, 2, createPatchFields(2, 3))
        player.place(patch, Position(0, 0))
        assertEquals(5, player.trackerPosition)

        // 5 Buttons start - 2 Button cost + 1 button income on patch
        // -> 3 button end result after
        // tracker advancement
        assertEquals(4, player.buttons)
    }
}
