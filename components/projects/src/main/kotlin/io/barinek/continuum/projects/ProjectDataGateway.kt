package io.barinek.continuum.projects

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement.RETURN_GENERATED_KEYS

@Repository
open class ProjectDataGateway(open val jdbcTemplate: JdbcTemplate) {

    fun create(accountId: Long, name: String): ProjectRecord {
        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(
                {
                    connection ->
                    val ps = connection.prepareStatement(
                            "insert into projects (account_id, name, active) values (?, ?, ?)", RETURN_GENERATED_KEYS)
                    ps.setLong(1, accountId)
                    ps.setString(2, name)
                    ps.setBoolean(3, true)
                    ps
                }, keyHolder)

        val id = keyHolder.key.toLong()

        return jdbcTemplate.queryForObject(
                "select id, account_id, name, active from projects where id = ?", arrayOf(id))
        { rs, num ->
            ProjectRecord(
                    id = rs.getLong("id"),
                    accountId = rs.getLong("account_id"),
                    name = rs.getString("name"),
                    active = rs.getBoolean("active"))
        }
    }

    fun findBy(accountId: Long): List<ProjectRecord> {
        val sql = "select id, account_id, name, active from projects where account_id = ? order by name asc"
        return jdbcTemplate.query(sql, arrayOf(accountId)) { rs, num -> ProjectRecord(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getBoolean(4)) }
    }

    fun findObject(projectId: Long): ProjectRecord? {
        val sql = "select id, account_id, name, active from projects where id = ? order by name asc"
        val list = jdbcTemplate.query(sql, arrayOf(projectId)) { rs, num -> ProjectRecord(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getBoolean(4)) }

        if (!list.isEmpty()) {
            return list.first()
        }
        return null
    }
}