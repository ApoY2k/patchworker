package apoy2k.patchworker.strategy

import apoy2k.patchworker.game.Game

interface Strategy {
    fun execute(game: Game)
}
