package apoy2k.patchworker.generator

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.Player
import apoy2k.patchworker.game.Position
import apoy2k.patchworker.saveGame
import apoy2k.patchworker.scoring.GameStateScorer
import org.postgresql.util.PSQLException

class GameStateGenerator(
    private val table: String,
    private val scorer: GameStateScorer,
) {

    fun generate(game: Game) {
        val currentPlayer = game.nextPlayer
        if (currentPlayer != null) {
            val options = createMoveOptionGameCopies(game, currentPlayer)

            val score = options
                .maxOf { scorer.score(it) }
                .toFloat()

            try {
                saveGame(game, score, table)
            } catch (ex: PSQLException) {
                // Truncate branches that are already duplicated in the database
                if (ex.sqlState == "23505") {
                    return
                }
                throw ex
            }

            options.forEach { generate(it) }
        }
    }

    private fun createMoveOptionGameCopies(game: Game, player: Player): List<Game> {
        val options = mutableListOf<Game>()

        for (patch in game.getPatchOptions()) {
            for ((rowIdx, fields) in player.board.withIndex()) {
                for ((colIdx, _) in fields.withIndex()) {
                    repeat(2) {
                        repeat(4) {
                            val anchor = Position(rowIdx, colIdx)
                            val childGame = game.copy()
                            if (childGame.place(patch, anchor)) {
                                options.add(childGame)
                            }
                            patch.rotate()
                        }
                        patch.rotate()
                        patch.flip()
                    }
                }
            }
        }

        val childGame = game.copy()
        options.add(childGame)
        childGame.advance()

        return options
    }
}
