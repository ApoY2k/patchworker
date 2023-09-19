package apoy2k.patchworker.game

private val buttonIncomeAtIndex = listOf(5, 11, 17, 23, 29, 35, 41, 47, 53)
private val specialTileAtIndex = listOf(26, 32, 38, 44, 50)

/**
 * Return Pair where
 *
 *    .first = the multiplier to board buttons
 *    .second = amount pf special tiles that were gained
 */
fun calculateIncome(startPosition: Int, steps: Int): Pair<Int, Int> {
    val buttonMultiplier = buttonIncomeAtIndex
        .count { it > startPosition && it <= startPosition + steps }
    val specialTiles = specialTileAtIndex
        .count { it > startPosition && it <= startPosition + steps }
    return Pair(buttonMultiplier, specialTiles)
}

fun getButtonMultiplier(position: Int) = buttonIncomeAtIndex.count { it > position }
