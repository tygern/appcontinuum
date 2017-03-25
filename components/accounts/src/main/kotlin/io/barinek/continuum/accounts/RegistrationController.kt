package io.barinek.continuum.accounts

import io.barinek.continuum.users.UserInfo
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class RegistrationController(val service: RegistrationService) {

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/registration")
    fun create(@RequestBody user: UserInfo): UserInfo {
        val record = service.createUserWithAccount(user.name)
        return UserInfo(record.id, record.name, "registration info")
    }
}