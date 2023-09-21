package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.scorePlayer
import apoy2k.patchworker.strategy.TryEverything

fun main() {
    val game = Game()
    val strategy = TryEverything()
    while (game.nextPlayer != null) {
        strategy.execute(game)
    }
    println("Game finished. Scores:")
    println("${game.player1}: ${scorePlayer(game.player1)}")
    println("${game.player2}: ${scorePlayer(game.player2)}")
}
