package io.barinek.continuum.accounts

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement.RETURN_GENERATED_KEYS

@Repository
class AccountDataGateway(val jdbcTemplate: JdbcTemplate) {
    fun create(ownerId: Long, name: String): AccountRecord {
        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update(
                {
                    connection ->
                    val ps = connection.prepareStatement(
                            "insert into accounts (owner_id, name) values (?, ?)", RETURN_GENERATED_KEYS)
                    ps.setLong(1, ownerId)
                    ps.setString(2, name)
                    ps
                }, keyHolder)

        val id = keyHolder.key.toLong()

        return jdbcTemplate.queryForObject(
                "select id, owner_id, name from accounts where id = ?", arrayOf(id))
        { rs, num ->
            AccountRecord(
                    id = rs.getLong("id"),
                    ownerId = rs.getLong("owner_id"),
                    name = rs.getString("name"))
        }
    }

    fun findBy(ownerId: Long): List<AccountRecord> {
        val sql = "select id, owner_id, name from accounts where owner_id = ? order by name desc limit 1"
        return jdbcTemplate.query(sql, arrayOf(ownerId)) { rs, num -> AccountRecord(rs.getLong(1), rs.getLong(2), rs.getString(3)) }
    }
}