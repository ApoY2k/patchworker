package apoy2k.patchworker.game

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PatchesTest {

    @Test
    fun `create standard patch set`() {
        val patches = generatePatches()
        assertEquals(33, patches.size)
    }
}
