package test.barinek.continuum

import io.barinek.continuum.restsupport.get
import io.barinek.continuum.restsupport.post
import io.barinek.continuum.testsupport.TestDataSourceConfig
import org.junit.Test
import org.springframework.web.client.RestTemplate
import java.io.File
import kotlin.test.assertEquals

class FlowTest {
    val dataSource = TestDataSourceConfig() // cleans the database
    val template = RestTemplate()
    val userDir = System.getProperty("user.dir")

    @Test
    fun testBasicFlow() {
        var response: String?

        val registrationServer = "http://localhost:8883"

        val registration = runCommand(8883, "java -jar $userDir/../registration-server/build/libs/registration-server-1.0-SNAPSHOT.jar", File(userDir))

        Thread.sleep(8000) // sorry, waiting for servers to start, bit longer for spring-webmvc

        response = template.get(registrationServer)
        assertEquals("Noop!", response)

        response = template.post("$registrationServer/registration", """{"name": "aUser"}""")
        val aUserId = findResponseId(response)
        assert(aUserId.toLong() > 0)

        response = template.get("$registrationServer/users?userId={userId}", Pair("userId", aUserId))
        assert(!response.isNullOrEmpty())

        response = template.get("$registrationServer/accounts?ownerId={ownerId}", Pair("ownerId", aUserId))
        val anAccountId = findResponseId(response)
        assert(anAccountId.toLong() > 0)

        response = template.post("$registrationServer/projects", """{"accountId":"$anAccountId","name":"aProject"}""")
        val aProjectId = findResponseId(response)
        assert(aProjectId.toLong() > 0)

        response = template.get("$registrationServer/projects?accountId={accountId}", Pair("accountId", anAccountId))
        assert(!response.isNullOrEmpty())

        ///

        val allocationsServer = "http://localhost:8881"

        val allocations = runCommand(8881, "java -jar $userDir/../allocations-server/build/libs/allocations-server-1.0-SNAPSHOT.jar", File(userDir))

        Thread.sleep(8000) // sorry, waiting for servers to start, bit longer for spring-webmvc

        response = template.get(allocationsServer)
        assertEquals("Noop!", response)

        response = template.post("$allocationsServer/allocations", """{"projectId":$aProjectId,"userId":$aUserId,"firstDay":"2015-05-17","lastDay":"2015-05-26"}""")
        val anAllocationId = findResponseId(response)
        assert(anAllocationId.toLong() > 0)

        response = template.get("$allocationsServer/allocations?projectId={projectId}", Pair("projectId", aProjectId))
        assert(!response.isNullOrEmpty())

        allocations.destroy()

        ///

        val backlogServer = "http://localhost:8882"

        val backlog = runCommand(8882, "java -jar $userDir/../backlog-server/build/libs/backlog-server-1.0-SNAPSHOT.jar", File(userDir))

        Thread.sleep(8000) // sorry, waiting for servers to start, bit longer for spring-webmvc

        response = template.get(backlogServer)
        assertEquals("Noop!", response)

        response = template.post("$backlogServer/stories", """{"projectId":$aProjectId,"name":"A story"}""")
        val aStoryId = findResponseId(response)
        assert(aStoryId.toLong() > 0)

        response = template.get("$backlogServer/stories?projectId={projectId}", Pair("projectId", aProjectId))
        assert(!response.isNullOrEmpty())

        backlog.destroy()

        ///

        val timesheetsServer = "http://localhost:8884"

        val timesheets = runCommand(8884, "java -jar $userDir/../timesheets-server/build/libs/timesheets-server-1.0-SNAPSHOT.jar", File(userDir))

        Thread.sleep(8000) // sorry, waiting for servers to start, bit longer for spring-webmvc

        response = template.get(timesheetsServer)
        assertEquals("Noop!", response)

        response = template.post("$timesheetsServer/time-entries", """{"projectId":$aProjectId,"userId":$aUserId,"date":"2015-05-17","hours":"8"}""")
        val aTimeEntryId = findResponseId(response)
        assert(aTimeEntryId.toLong() > 0)

        response = template.get("$timesheetsServer/time-entries?projectId={projectId}", Pair("projectId", aUserId))
        assert(!response.isNullOrEmpty())

        timesheets.destroy()

        ///

        registration.destroy() // dependency of above 3
    }

    /// Test Support

    private fun findResponseId(response: String) = Regex("id\":(\\d+),").find(response)?.groupValues!![1]

    private fun runCommand(port: Int, command: String, workingDir: File): Process {
        val builder = ProcessBuilder(*command.split(" ").toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
        builder.environment()["PORT"] = port.toString()
        return builder.start()
    }
}

