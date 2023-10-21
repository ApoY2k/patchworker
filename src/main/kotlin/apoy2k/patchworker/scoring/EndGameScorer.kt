package apoy2k.patchworker.scoring

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.scorePlayer

class EndGameScorer : GameStateScorer {
    override fun score(game: Game): Double = (scorePlayer(game.player1) - scorePlayer(game.player2)).toDouble()
}
