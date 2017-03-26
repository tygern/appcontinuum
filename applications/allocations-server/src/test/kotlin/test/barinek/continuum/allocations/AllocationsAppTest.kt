package test.barinek.continuum.allocations

import io.barinek.continuum.allocations.App
import io.barinek.continuum.restsupport.get
import org.junit.Test
import org.springframework.boot.SpringApplication
import org.springframework.web.client.RestTemplate
import java.util.*
import kotlin.test.assertEquals

class AllocationsAppTest {
    @Test
    fun embedded() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        System.setProperty("server.port", System.getenv("PORT"))
        SpringApplication.run(App::class.java, *arrayOf<String>())

        val template = RestTemplate()

        assertEquals("[]", template.get("http://localhost:8081/allocations?projectId=0"))
    }
}