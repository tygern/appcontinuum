package io.barinek.continuum.users


import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import java.sql.Statement.RETURN_GENERATED_KEYS

class UserDataGateway(val jdbcTemplate: JdbcTemplate) {
    fun create(name: String): UserRecord {
        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(
                {
                    connection ->
                    val ps = connection.prepareStatement(
                            "insert into users (name) values (?)", RETURN_GENERATED_KEYS)
                    ps.setString(1, name)
                    ps
                }, keyHolder)

        val id = keyHolder.key.toLong()

        return jdbcTemplate.queryForObject(
                "select id, name, name from users where id = ?", arrayOf(id))
        { rs, num ->
            UserRecord(
                    id = rs.getLong("id"),
                    name = rs.getString("name"))
        }
    }

    fun findObjectBy(id: Long): UserRecord {
        val s = "select id, name from users where id = ? limit 1"
        val find = jdbcTemplate.query(s, arrayOf(id)) { rs, num -> UserRecord(rs.getLong(1), rs.getString(2)) }
        return find[0]
    }
}