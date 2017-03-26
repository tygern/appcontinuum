package io.barinek.continuum.projects

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
open class ProjectController(open val gateway: ProjectDataGateway) {

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/projects")
    fun create(@RequestBody project: ProjectInfo): ResponseEntity<ProjectInfo> {
        val record = gateway.create(project.accountId, project.name)
        val info = ProjectInfo(record.id, record.accountId, record.name, record.active, "project info")
        return ResponseEntity(info, HttpStatus.CREATED)
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/projects")
    fun list(@RequestParam accountId: String): List<ProjectInfo> {
        return gateway.findBy(accountId.toLong()).map { record ->
            ProjectInfo(record.id, record.accountId, record.name, record.active, "project info")
        }
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/project")
    fun get(@RequestParam projectId: String): ProjectInfo? {
        val record = gateway.findObject(projectId.toLong())
        if (record != null) {
            return ProjectInfo(record.id, record.accountId, record.name, record.active, "project info")
        }
        return null
    }
}