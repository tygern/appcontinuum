package io.barinek.continuum.backlog

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement.RETURN_GENERATED_KEYS

@Repository
class StoryDataGateway(val jdbcTemplate: JdbcTemplate) {

    fun create(projectId: Long, name: String): StoryRecord {
        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(
                {
                    connection ->
                    val ps = connection.prepareStatement(
                            "insert into stories (project_id, name) values (?, ?)", RETURN_GENERATED_KEYS)
                    ps.setLong(1, projectId)
                    ps.setString(2, name)
                    ps
                }, keyHolder)

        val id = keyHolder.key.toLong()

        return jdbcTemplate.queryForObject(
                "select id, project_id, name from stories where id = ?", arrayOf(id))
        { rs, num ->
            StoryRecord(
                    id = rs.getLong("id"),
                    projectId = rs.getLong("project_id"),
                    name = rs.getString("name"))
        }
    }

    fun findBy(projectId: Long): List<StoryRecord> {
        val sql = "select id, project_id, name from stories where project_id = ?"
        return jdbcTemplate.query(sql, arrayOf(projectId)) { rs, num -> StoryRecord(rs.getLong(1), rs.getLong(2), rs.getString(3)) }
    }
}