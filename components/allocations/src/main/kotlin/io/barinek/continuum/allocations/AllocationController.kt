package io.barinek.continuum.allocations

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
open class AllocationController(open val gateway: AllocationDataGateway, open val client: ProjectClient) {

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/allocations")
    fun create(@RequestBody allocation: AllocationInfo): ResponseEntity<AllocationInfo> {
        if (projectIsActive(allocation.projectId)) {
            val record = gateway.create(allocation.projectId, allocation.userId, allocation.firstDay, allocation.lastDay)
            val info = AllocationInfo(record.id, record.projectId, record.userId, record.firstDay, record.lastDay, "allocation info")
            return ResponseEntity(info, HttpStatus.CREATED)
        }
        return ResponseEntity(HttpStatus.NOT_MODIFIED)
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/allocations")
    fun list(@RequestParam projectId: String): List<AllocationInfo> {
        return gateway.findBy(projectId.toLong()).map { record ->
            AllocationInfo(record.id, record.projectId, record.userId, record.firstDay, record.lastDay, "allocation info")
        }
    }

    private fun projectIsActive(projectId: Long): Boolean {
        val project = client.getProject(projectId)
        return project != null && project.active
    }
}