package test.barinek.continuum.backlog

import com.fasterxml.jackson.core.type.TypeReference
import io.barinek.continuum.backlog.ProjectInfo
import io.barinek.continuum.backlog.StoryInfo
import io.barinek.continuum.restsupport.get
import io.barinek.continuum.restsupport.post
import io.barinek.continuum.testsupport.TestApp
import io.barinek.continuum.testsupport.TestControllerSupport
import io.barinek.continuum.testsupport.TestScenarioSupport
import io.barinek.continuum.testsupport.TestSpringApp
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class StoryControllerTest : TestControllerSupport() {
    val app = TestSpringApp(8081, "io.barinek.continuum.backlog",
            "io.barinek.continuum.jdbcsupport",
            "io.barinek.continuum.restsupport",
            "io.barinek.continuum.testsupport")

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

        val json = "{\"projectId\":55432,\"name\":\"An epic story\"}"
        val response = template.post("http://localhost:8081/stories", json)
        val actual = mapper.readValue(response, StoryInfo::class.java)

        assert(actual.id > 0)
        assertEquals(55432L, actual.projectId)
        assertEquals("An epic story", actual.name)
        assertEquals("story info", actual.info)

        uServiceProjects.stop()
    }

    @Test
    fun testFailedCreate() {
        TestScenarioSupport().loadTestScenario("jacks-test-scenario")

        val uServiceProjects = TestApp(8883) { mapper, outputStream ->
            mapper.writeValue(outputStream, ProjectInfo(false))
        }
        uServiceProjects.start()

        val json = "{\"projectId\":55432,\"name\":\"An epic story\"}"
        val response = template.post("http://localhost:8081/stories", json)
        assert(response.isBlank())

        uServiceProjects.stop()
    }

    @Test
    fun testFind() {
        TestScenarioSupport().loadTestScenario("jacks-test-scenario")

        val response = template.get("http://localhost:8081/stories?projectId={projectId}", Pair("projectId", "55432"))
        val stories: List<StoryInfo> = mapper.readValue(response, object : TypeReference<List<StoryInfo>>() {})
        val actual = stories.first()

        assertEquals(876L, actual.id)
        assertEquals(55432L, actual.projectId)
        assertEquals("An epic story", actual.name)
        assertEquals("story info", actual.info)
    }
}