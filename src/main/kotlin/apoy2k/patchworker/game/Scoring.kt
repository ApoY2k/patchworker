package apoy2k.patchworker.game

fun scoreBoard(board: Fields, buttonMultiplier: Int) = board
    .flatten()
    .map {
        when (it) {
            1 -> it * buttonMultiplier
            else -> it
        }
    }
    .reduce { acc, field -> acc + field }

fun scorePlayer(player: Player) =
    scoreBoard(player.board, getButtonMultiplier(player.trackerPosition)) +
            player.buttons
