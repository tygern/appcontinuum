package io.barinek.continuum.allocations

import com.fasterxml.jackson.databind.ObjectMapper
import io.barinek.continuum.restsupport.get
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
open class ProjectClient(val mapper: ObjectMapper, val template: RestTemplate) {
    open fun getProject(projectId: Long): ProjectInfo? {
        val endpoint = System.getenv("REGISTRATION_SERVER_ENDPOINT")
        val response = template.get("$endpoint/project?projectId={projectId}", Pair("projectId", projectId.toString()))
        return mapper.readValue(response, ProjectInfo::class.java)
    }
}