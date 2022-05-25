package com.bsfdv.backend.infrastructure.persistence

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class FlywayRunner(private val flyway: Flyway) : InitializingBean {

    override fun afterPropertiesSet() {
        flyway.migrate()
    }
}