package apoy2k.patchworker

import apoy2k.patchworker.game.Fields
import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.scorePlayer
import apoy2k.patchworker.strategy.TryEverything

fun main() {
    val game = Game()
    val strategy = TryEverything()
    while (game.nextPlayer != null) {
        strategy.execute(game)
    }
    println("Game finished. ${game.getRemainingPatches()} patches remain")
    println("${game.player1}: ${scorePlayer(game.player1)}")
    printBoard(game.player1.board)
    println("${game.player2}: ${scorePlayer(game.player2)}")
    printBoard(game.player2.board)
}

private fun printBoard(fields: Fields) {
    fields.forEach { row ->
        row.forEach { field ->
            when (field) {
                true -> print(" X ")
                else -> print("   ")
            }
        }
        print("\n")
    }
}
