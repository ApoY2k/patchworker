package apoy2k.patchworker.game

fun scoreBoard(board: Fields) = board.flatten().sumOf {
    @Suppress("USELESS_CAST") // https://youtrack.jetbrains.com/issue/KT-46360
    when (it) {
        true -> 0
        else -> -2
    } as Int
}

fun scorePlayer(player: Player) = scoreBoard(player.board) + player.buttons
