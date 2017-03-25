package io.barinek.continuum.accounts

import io.barinek.continuum.users.UserDataGateway
import io.barinek.continuum.users.UserRecord
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.Transactional

@Configuration
open class RegistrationService(val userDataGateway: UserDataGateway, val accountDataGateway: AccountDataGateway) {

    @Transactional
    fun createUserWithAccount(name: String): UserRecord {
        val user = userDataGateway.create(name)
        accountDataGateway.create(user.id, String.format("%s's account", name))
        return user
    }
}