package test.barinek.continuum.registration

import io.barinek.continuum.registration.App
import io.barinek.continuum.restsupport.get
import org.junit.Test
import org.springframework.boot.SpringApplication
import org.springframework.web.client.RestTemplate
import java.util.*
import kotlin.test.assertEquals

class RegistrationAppTest {
    @Test
    fun embedded() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        System.setProperty("server.port", System.getenv("PORT"))
        SpringApplication.run(App::class.java, *arrayOf<String>())

        val template = RestTemplate()

        assertEquals("[]", template.get("http://localhost:8081/accounts?ownerId=0"))
        assertEquals("[]", template.get("http://localhost:8081/projects?accountId=0"))
        assertEquals("", template.get("http://localhost:8081/project?projectId=0"))
        assertEquals("", template.get("http://localhost:8081/users?userId=0"))
    }
}