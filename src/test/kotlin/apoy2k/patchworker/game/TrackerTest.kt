package apoy2k.patchworker.game

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class TrackerTest {

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

    @ParameterizedTest
    @CsvSource(
        "3, 2, 0",
        "22, 7, 1",
    )
    fun `count special tiles incomes`(initialSteps: Int, advanceSteps: Int, expectedIncomes: Int) {
        assertEquals(expectedIncomes, calculateIncome(initialSteps, advanceSteps).second)
    }
}
