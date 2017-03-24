package io.barinek.continuum.allocations

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import java.sql.Date
import java.sql.Statement.RETURN_GENERATED_KEYS
import java.time.LocalDate

class AllocationDataGateway(val jdbcTemplate: JdbcTemplate) {

    fun create(projectId: Long, userId: Long, firstDay: LocalDate, lastDay: LocalDate): AllocationRecord {
        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(
                {
                    connection ->
                    val ps = connection.prepareStatement(
                            "insert into allocations (project_id, user_id, first_day, last_day) values (?, ?, ?, ?)", RETURN_GENERATED_KEYS)
                    ps.setLong(1, projectId)
                    ps.setLong(2, userId)
                    ps.setDate(3, Date.valueOf(firstDay))
                    ps.setDate(4, Date.valueOf(lastDay))
                    ps
                }, keyHolder)

        val id = keyHolder.key.toLong()

        return jdbcTemplate.queryForObject(
                "select id, project_id, user_id, first_day, last_day from allocations where id = ?", arrayOf(id))
        { rs, num ->
            AllocationRecord(
                    id = rs.getLong("id"),
                    projectId = rs.getLong("project_id"),
                    userId = rs.getLong("user_id"),
                    firstDay = rs.getDate("first_day").toLocalDate(),
                    lastDay = rs.getDate("last_day").toLocalDate())
        }
    }

    fun findBy(projectId: Long): List<AllocationRecord> {
        val sql = "select id, project_id, user_id, first_day, last_day from allocations where project_id = ? order by first_day"
        return jdbcTemplate.query(sql, arrayOf(projectId)) { rs, rowNum -> AllocationRecord(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getDate(4).toLocalDate(), rs.getDate(5).toLocalDate()) }
    }
}