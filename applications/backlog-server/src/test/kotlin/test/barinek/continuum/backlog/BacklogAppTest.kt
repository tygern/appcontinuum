package test.barinek.continuum.backlog

import io.barinek.continuum.backlog.App
import io.barinek.continuum.restsupport.get
import org.junit.Test
import org.springframework.boot.SpringApplication
import org.springframework.web.client.RestTemplate
import java.util.*
import kotlin.test.assertEquals

class BacklogAppTest {
    @Test
    fun embedded() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        System.setProperty("server.port", System.getenv("PORT"))
        SpringApplication.run(App::class.java, *arrayOf<String>())

        val template = RestTemplate()

        assertEquals("[]", template.get("http://localhost:8081/stories?projectId=0"))
    }
}