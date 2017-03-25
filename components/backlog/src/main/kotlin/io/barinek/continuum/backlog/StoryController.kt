package io.barinek.continuum.backlog

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class StoryController(val gateway: StoryDataGateway, val client: ProjectClient) {

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/stories")
    fun create(@RequestBody story: StoryInfo): ResponseEntity<StoryInfo> {
        if (projectIsActive(story.projectId)) {
            val record = gateway.create(story.projectId, story.name)
            val info = StoryInfo(record.id, record.projectId, record.name, "story info")
            return ResponseEntity(info, HttpStatus.CREATED)
        }
        return ResponseEntity(HttpStatus.NOT_MODIFIED)
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/stories")
    fun list(@RequestParam projectId: String): List<StoryInfo> {
        return gateway.findBy(projectId.toLong()).map { record ->
            StoryInfo(record.id, record.projectId, record.name, "story info")
        }
    }

    private fun projectIsActive(projectId: Long): Boolean {
        val project = client.getProject(projectId)
        return project != null && project.active
    }
}