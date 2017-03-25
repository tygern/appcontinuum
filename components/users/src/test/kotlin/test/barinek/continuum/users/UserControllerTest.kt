package test.barinek.continuum.users

import io.barinek.continuum.TestControllerSupport
import io.barinek.continuum.TestScenarioSupport
import io.barinek.continuum.restsupport.SpringApp
import io.barinek.continuum.restsupport.get
import io.barinek.continuum.users.UserInfo
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UserControllerTest : TestControllerSupport() {
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
    fun testShow() {
        TestScenarioSupport().loadTestScenario("jacks-test-scenario")

        val response = template.get("http://localhost:8081/users?userId={userId}", Pair("userId", "4765"))
        val actual = mapper.readValue(response, UserInfo::class.java)

        assertEquals(4765L, actual.id)
        assertEquals("Jack", actual.name)
        assertEquals("user info", actual.info)
    }
}