package io.barinek.continuum.jdbcsupport

import com.fasterxml.jackson.databind.ObjectMapper
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
open class DataSourceConfig {

    @Bean
    open fun getJdbcTemplate(): JdbcTemplate {
        return JdbcTemplate(createDataSource())
    }

    fun createDataSource(): HikariDataSource {
        val json = System.getenv("VCAP_SERVICES")
        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = from(json)
        return dataSource
    }

    fun from(json: String): String? {
        val mapper = ObjectMapper()
        val root = mapper.readTree(json)
        val mysql = root.findValue("p-mysql")
        val credentials = mysql.findValue("credentials")
        return credentials.findValue("jdbcUrl").textValue()
    }
}