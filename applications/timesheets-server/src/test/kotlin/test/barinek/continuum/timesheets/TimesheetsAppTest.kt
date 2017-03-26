package test.barinek.continuum.timesheets

import io.barinek.continuum.restsupport.get
import io.barinek.continuum.timesheets.App
import org.junit.Test
import org.springframework.boot.SpringApplication
import org.springframework.web.client.RestTemplate
import java.util.*
import kotlin.test.assertEquals

class TimesheetsAppTest {
    @Test
    fun embedded() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        System.setProperty("server.port", System.getenv("PORT"))
        SpringApplication.run(App::class.java, *arrayOf<String>())

        val template = RestTemplate()

        assertEquals("[]", template.get("http://localhost:8081/time-entries?projectId=0"))
    }
}