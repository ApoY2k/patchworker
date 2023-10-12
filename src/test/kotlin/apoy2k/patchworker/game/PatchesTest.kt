package apoy2k.patchworker.game

import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.test.assertEquals

class PatchesTest {

    @Test
    fun `create standard patch set`() {
        val patches = generatePatches()
        assertEquals(33, patches.size)
    }

    @Test
    fun `build patches list checksum`() {
        val patches = listOf(createPatch_5X3_X(), createPatch_3X2_Z2(), createPatch_4X3_O())
        assertEquals(BigInteger("0000000000001000000000000000010001", 2).toString(), patches.checksum())
    }
}
