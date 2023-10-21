package apoy2k.patchworker

import apoy2k.patchworker.game.Game
import apoy2k.patchworker.game.checksum
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

val datasource = HikariDataSource(HikariConfig().also {
    it.jdbcUrl = System.getenv("JDBC_URL")
    it.username = "postgres"
    it.password = "postgres"
})

const val INSERT_DATA_STATEMENT = """
    insert into %s
     (
        patches,
        p1_tracker_position, p1_button_multiplier, p1_buttons, p1_special_patches, p1_actions_taken, p1_board,
        p2_tracker_position, p2_button_multiplier, p2_buttons, p2_special_patches, p2_actions_taken, p2_board,
        is_p1_turn,
        score
     )
     values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
"""

fun saveGame(game: Game, score: Float, table: String) {
    datasource.connection.use { connection ->
        val query = INSERT_DATA_STATEMENT.format(table)
        connection.prepareStatement(query).use { statement ->
            statement.setString(1, game.patchesChecksum())
            statement.setInt(2, game.player1.trackerPosition)
            statement.setInt(3, game.player1.buttonMultiplier)
            statement.setInt(4, game.player1.buttons)
            statement.setInt(5, game.player1.specialPatches)
            statement.setInt(6, game.player1.actionsTaken)
            statement.setString(7, game.player1.board.checksum())
            statement.setInt(8, game.player2.trackerPosition)
            statement.setInt(9, game.player2.buttonMultiplier)
            statement.setInt(10, game.player2.buttons)
            statement.setInt(11, game.player2.specialPatches)
            statement.setInt(12, game.player2.actionsTaken)
            statement.setString(13, game.player2.board.checksum())
            statement.setBoolean(14, game.isPlayer1Turn())
            statement.setFloat(15, score)
            statement.executeUpdate()
        }
    }
}
