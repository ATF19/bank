package com.bsfdv.backend.infrastructure.springboot

import com.bsfdv.backend.application.core.ApplicationService
import com.bsfdv.backend.domain.service.core.DomainService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType

@ComponentScan(
    includeFilters = [ComponentScan.Filter(
        type = FilterType.ANNOTATION,
        classes = [DomainService::class, ApplicationService::class]
    )],
    basePackages = ["com.bsfdv.backend"]
)
@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
	runApplication<BackendApplication>(*args)
}
