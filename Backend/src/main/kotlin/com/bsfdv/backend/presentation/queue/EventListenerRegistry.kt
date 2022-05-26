package com.bsfdv.backend.presentation.queue

import com.google.common.eventbus.EventBus
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class EventListenerRegistry(val listeners: List<EventBusListener>, val eventBus: EventBus) : ApplicationRunner {

    private val logger = Logger.getLogger(this::class.qualifiedName)

    override fun run(args: ApplicationArguments?) {
        for (listener in listeners) {
            logger.info("Registering event listener ${listener::class.simpleName}.")
            eventBus.register(listener)
        }
    }

}