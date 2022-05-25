package com.bsfdv.backend.infrastructure.eventbus

import com.google.common.eventbus.EventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventBusConfiguration {

    @Bean
    fun eventBus() = EventBus()

}