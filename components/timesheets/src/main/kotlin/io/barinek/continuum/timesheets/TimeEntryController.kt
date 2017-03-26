package io.barinek.continuum.timesheets

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
open class TimeEntryController(open val gateway: TimeEntryDataGateway, open val client: ProjectClient) {

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/time-entries")
    fun create(@RequestBody entry: TimeEntryInfo): ResponseEntity<TimeEntryInfo> {
        if (projectIsActive(entry.projectId)) {
            val record = gateway.create(entry.projectId, entry.userId, entry.date, entry.hours)
            val info = TimeEntryInfo(record.id, record.projectId, record.userId, record.date, record.hours, "entry info")
            return ResponseEntity(info, HttpStatus.CREATED)
        }
        return ResponseEntity(HttpStatus.NOT_MODIFIED)
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/time-entries")
    fun list(@RequestParam projectId: String): List<TimeEntryInfo> {
        return gateway.findBy(projectId.toLong()).map { record ->
            TimeEntryInfo(record.id, record.projectId, record.userId, record.date, record.hours, "entry info")
        }
    }

    private fun projectIsActive(projectId: Long): Boolean {
        val project = client.getProject(projectId)
        return project != null && project.active
    }
}