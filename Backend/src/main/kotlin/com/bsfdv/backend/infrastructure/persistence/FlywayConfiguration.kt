package com.bsfdv.backend.infrastructure.persistence

import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class FlywayConfiguration(private val dataSource: DataSource) {

    @Bean
    fun flyway(): Flyway {
        return Flyway
            .configure()
            .baselineOnMigrate(true)
            .dataSource(dataSource)
            .createSchemas(true)
            .load()
    }
}