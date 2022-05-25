package com.bsfdv.backend.presentation.rest

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiDocumentationConfiguration(@Value("\${info.app.version}") private val version: String) {

    @Bean
    fun openAPI() = OpenAPI()
        .components(Components())
        .info(Info().title("BSFDV Backend API").description("").version(version))

}