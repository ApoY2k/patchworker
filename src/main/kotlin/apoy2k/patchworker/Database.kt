package apoy2k.patchworker

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
