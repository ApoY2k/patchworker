package apoy2k.patchworker.scoring

import apoy2k.patchworker.game.Game

interface GameStateScorer {
    fun score(game: Game): Double
}
