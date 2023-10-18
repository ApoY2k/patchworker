package apoy2k.patchworker

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

val datasource = HikariDataSource(HikariConfig().also {
    it.jdbcUrl = System.getenv("JDBC_URL")
    it.username = "postgres"
    it.password = "postgres"
})
