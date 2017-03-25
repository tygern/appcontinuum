package test.barinek.continuum.timesheets

import com.fasterxml.jackson.core.type.TypeReference
import io.barinek.continuum.TestApp
import io.barinek.continuum.TestControllerSupport
import io.barinek.continuum.TestScenarioSupport
import io.barinek.continuum.restsupport.SpringApp
import io.barinek.continuum.restsupport.get
import io.barinek.continuum.restsupport.post
import io.barinek.continuum.timesheets.ProjectInfo
import io.barinek.continuum.timesheets.TimeEntryInfo
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class TimeEntryControllerTest : TestControllerSupport() {
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
    fun testCreate() {
        TestScenarioSupport().loadTestScenario("jacks-test-scenario")

        val uServiceProjects = TestApp(8883) { mapper, outputStream ->
            mapper.writeValue(outputStream, ProjectInfo(true))
        }
        uServiceProjects.start()


        val json = "{\"projectId\":55432,\"userId\":4765,\"date\":\"2015-05-17\",\"hours\":8}"
        val response = template.post("http://localhost:8081/time-entries", json)

        val actual = mapper.readValue(response, TimeEntryInfo::class.java)

        assert(actual.id > 0)
        assertEquals(55432L, actual.projectId)
        assertEquals(4765L, actual.userId)
        assertEquals(8, actual.hours)

        uServiceProjects.stop()
    }

    @Test
    fun testFailedCreate() {
        TestScenarioSupport().loadTestScenario("jacks-test-scenario")

        val uServiceProjects = TestApp(8883) { mapper, outputStream ->
            mapper.writeValue(outputStream, ProjectInfo(false))
        }
        uServiceProjects.start()

        val json = "{\"projectId\":55432,\"userId\":4765,\"date\":\"2015-05-17\",\"hours\":8}"
        val response = template.post("http://localhost:8081/time-entries", json)
        assert(response.isBlank())

        uServiceProjects.stop()
    }

    @Test
    fun testFind() {
        TestScenarioSupport().loadTestScenario("jacks-test-scenario")

        val response = template.get("http://localhost:8081/time-entries?projectId={projectId}", Pair("projectId", "4765"))
        val stories: List<TimeEntryInfo> = mapper.readValue(response, object : TypeReference<List<TimeEntryInfo>>() {})
        val actual = stories.first()

        assertEquals(1534L, actual.id)
        assertEquals(55432L, actual.projectId)
        assertEquals(4765L, actual.userId)
        assertEquals(LocalDate.of(2015, 5, 17), actual.date)
        assertEquals(5, actual.hours)
    }
}