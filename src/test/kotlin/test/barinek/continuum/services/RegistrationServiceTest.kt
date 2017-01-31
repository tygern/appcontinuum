package test.barinek.continuum.services

import io.barinek.continuum.dataaccess.AccountDataGateway
import io.barinek.continuum.dataaccess.UserDataGateway
import io.barinek.continuum.services.RegistrationService
import io.barinek.continuum.util.JdbcTemplate
import io.barinek.continuum.util.TransactionManager
import org.junit.Test
import test.barinek.continuum.util.TestDataSourceConfig
import kotlin.test.assertEquals

class RegistrationServiceTest() {
    @Test
    fun testCreateUserWithAccount() {
        val dataSource = TestDataSourceConfig().dataSource
        val template = JdbcTemplate(dataSource)
        val transactionManager = TransactionManager(dataSource)

        val usersGateway = UserDataGateway(template)
        val accountsGateway = AccountDataGateway(template)

        val service = RegistrationService(transactionManager, usersGateway, accountsGateway)
        val aUser = service.createUserWithAccount("aUser")

        assertEquals("aUser", aUser.name)
    }
}