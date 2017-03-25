package test.barinek.continuum.accounts

import io.barinek.continuum.TestControllerSupport
import io.barinek.continuum.TestScenarioSupport
import io.barinek.continuum.restsupport.SpringApp
import io.barinek.continuum.restsupport.post
import io.barinek.continuum.users.UserInfo
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class RegistrationControllerTest : TestControllerSupport() {
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
    fun testRegister() {
        TestScenarioSupport().loadTestScenario("jacks-test-scenario")

        val registrationResponse = template.post("http://localhost:8081/registration", "{\"name\":\"aUser\"}")
        val actual = mapper.readValue(registrationResponse, UserInfo::class.java)

        assert(actual.id > 0)
        assertEquals("aUser", actual.name)
        assertEquals("registration info", actual.info)
    }
}