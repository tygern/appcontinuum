package io.barinek.continuum.users

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(val gateway: UserDataGateway) {

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/users")
    fun list(@RequestParam userId: String): UserInfo {
        val record = gateway.findObjectBy(userId.toLong())
        return UserInfo(record.id, record.name, "user info")
    }
}