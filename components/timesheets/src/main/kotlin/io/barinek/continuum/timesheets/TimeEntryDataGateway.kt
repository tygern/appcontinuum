package io.barinek.continuum.timesheets

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.Date
import java.sql.Statement.RETURN_GENERATED_KEYS
import java.time.LocalDate

@Repository
class TimeEntryDataGateway(val jdbcTemplate: JdbcTemplate) {

    fun create(projectId: Long, userId: Long, date: LocalDate, hours: Int): TimeEntryRecord {
        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(
                {
                    connection ->
                    val ps = connection.prepareStatement(
                            "insert into time_entries (project_id, user_id, date, hours) values (?, ?, ?, ?)", RETURN_GENERATED_KEYS)
                    ps.setLong(1, projectId)
                    ps.setLong(2, userId)
                    ps.setDate(3, Date.valueOf(date))
                    ps.setInt(4, hours)
                    ps
                }, keyHolder)

        val id = keyHolder.key.toLong()

        return jdbcTemplate.queryForObject(
                "select id, project_id, user_id, date, hours from time_entries where id = ?", arrayOf(id))
        { rs, num ->
            TimeEntryRecord(
                    id = rs.getLong("id"),
                    projectId = rs.getLong("project_id"),
                    userId = rs.getLong("user_id"),
                    date = rs.getDate("date").toLocalDate(),
                    hours = rs.getInt("hours"))
        }
    }

    fun findBy(userId: Long): List<TimeEntryRecord> {
        val sql = "select id, project_id, user_id, date, hours from time_entries where user_id = ?"
        return jdbcTemplate.query(sql, arrayOf(userId)) { rs, num -> TimeEntryRecord(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getDate(4).toLocalDate(), rs.getInt(5)) }
    }
}