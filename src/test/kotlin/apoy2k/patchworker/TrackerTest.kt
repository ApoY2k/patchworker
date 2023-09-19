package apoy2k.patchworker

import apoy2k.patchworker.game.calculateIncome
import apoy2k.patchworker.game.getButtonMultiplier
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class TrackerTest {

    @ParameterizedTest
    @CsvSource(
        "3, 9",
        "22, 6",
        "48, 1"
    )
    fun `find button multiplier`(advanceSteps: Int, expectedMultiplier: Int) {
        calculateIncome(0, advanceSteps)
        assertEquals(expectedMultiplier, getButtonMultiplier(advanceSteps))
    }

    @ParameterizedTest
    @CsvSource(
        "3, 0",
        "22, 3",
        "48, 8"
    )
    fun `count button incomes`(advanceSteps: Int, expectedIncomes: Int) {
        assertEquals(expectedIncomes, calculateIncome(0, advanceSteps).first)
    }

    @ParameterizedTest
    @CsvSource(
        "3, 2, 1",
        "22, 7, 2",
        "34, 15, 3",
        "48, 5, 1",
    )
    fun `count button incomes for multiple advances`(initialSteps: Int, advanceSteps: Int, expectedIncomes: Int) {
        assertEquals(expectedIncomes, calculateIncome(initialSteps, advanceSteps).first)
    }
}
