package test.barinek.continuum.accounts

import com.fasterxml.jackson.core.type.TypeReference
import io.barinek.continuum.TestControllerSupport
import io.barinek.continuum.TestScenarioSupport
import io.barinek.continuum.accounts.AccountInfo
import io.barinek.continuum.restsupport.SpringApp
import io.barinek.continuum.restsupport.get
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AccountControllerTest : TestControllerSupport() {
    val app = SpringApp(8081, "io.barinek.continuum")

    @Before
    fun setUp() {
        app.start()
    }

    @After
    fun tearDown() {
        app.stop()
    }

    @Test
    fun testFind() {
        TestScenarioSupport().loadTestScenario("jacks-test-scenario")

        val ownerId = Pair("ownerId", "4765")
        val response = template.get("http://localhost:8081/accounts?ownerId={ownerId}", ownerId)
        val list: List<AccountInfo> = mapper.readValue(response, object : TypeReference<List<AccountInfo>>() {})
        val actual = list.first()

        assertEquals(1673L, actual.id)
        assertEquals(4765L, actual.ownerId)
        assertEquals("Jack's account", actual.name)
        assertEquals("account info", actual.info)
    }
}