package io.barinek.continuum.restsupport

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class SpringDefaultController {
    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/")
    @ResponseBody
    fun default(): ResponseEntity<String> {
        return ResponseEntity("Noop!", HttpStatus.OK)
    }
}