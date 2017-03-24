package test.barinek.continuum.accounts

import io.barinek.continuum.TestDataSourceConfig
import io.barinek.continuum.accounts.AccountDataGateway
import io.barinek.continuum.accounts.RegistrationService
import io.barinek.continuum.users.UserDataGateway
import org.junit.Test
import org.springframework.jdbc.core.JdbcTemplate
import kotlin.test.assertEquals

class RegistrationServiceTest() {
    @Test
    fun testCreateUserWithAccount() {
        val dataSource = TestDataSourceConfig().dataSource
        val template = JdbcTemplate(dataSource)

        val usersGateway = UserDataGateway(template)
        val accountsGateway = AccountDataGateway(template)

        val service = RegistrationService(usersGateway, accountsGateway)
        val aUser = service.createUserWithAccount("aUser")

        assertEquals("aUser", aUser.name)
    }
}