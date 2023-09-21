package apoy2k.patchworker

import apoy2k.patchworker.game.Game

fun main() {
    val game = Game()
    recurseGameSteps(game, 0, 5)
}
