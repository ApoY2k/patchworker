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
        hash, parenthash, patches,
        p1_tp, p1_bm, p1_bs, p1_sp, p1_at, p1_bd,
        p2_tp, p2_bm, p2_bs, p2_sp, p2_at, p2_bd,
        p1_tn, score
     )
     values
     (
        ?, ?, ?,
        ?, ?, ?, ?, ?, ?,
        ?, ?, ?, ?, ?, ?,
        ?, ?
     )
"""

fun saveGame(game: Game, score: Float, table: String) {
    datasource.connection.use { connection ->
        val query = INSERT_DATA_STATEMENT.format(table)
        connection.prepareStatement(query).use { statement ->
            statement.setInt(1, game.hashCode())
            statement.setInt(2, game.parentHash)
            statement.setString(3, game.patchesList())
            statement.setInt(4, game.player1.trackerPosition)
            statement.setInt(5, game.player1.buttonMultiplier)
            statement.setInt(6, game.player1.buttons)
            statement.setInt(7, game.player1.specialPatches)
            statement.setInt(8, game.player1.actionsTaken)
            statement.setString(9, game.player1.board.checksum())
            statement.setInt(10, game.player2.trackerPosition)
            statement.setInt(11, game.player2.buttonMultiplier)
            statement.setInt(12, game.player2.buttons)
            statement.setInt(13, game.player2.specialPatches)
            statement.setInt(14, game.player2.actionsTaken)
            statement.setString(15, game.player2.board.checksum())
            statement.setBoolean(16, game.isPlayer1Turn())
            statement.setFloat(17, score)
            statement.executeUpdate()
        }
    }
}
