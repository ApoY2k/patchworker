package apoy2k.patchworker.game

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GameTest {

    @Test
    fun `patch options`() {
        val game = Game()
        val options = game.getPatchOptions()
        assertEquals(3, options.size)
    }
}
