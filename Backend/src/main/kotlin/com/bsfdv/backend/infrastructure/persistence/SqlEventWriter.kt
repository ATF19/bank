package com.bsfdv.backend.infrastructure.persistence

import com.bsfdv.backend.domain.model.core.DomainEntity
import com.bsfdv.backend.domain.model.core.Event
import com.bsfdv.backend.domain.model.core.UnitOfWork
import com.bsfdv.backend.domain.service.core.EventWriter
import com.bsfdv.backend.infrastructure.mapping.JsonMapper
import com.google.common.eventbus.EventBus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import kotlin.reflect.KClass

@Repository
class SqlEventWriter(
    private val jdbcTemplate: JdbcTemplate, private val jsonMapper: JsonMapper,
    private val eventBus: EventBus
) : EventWriter {

    override fun save(unitOfWork: UnitOfWork) {
        val eventsToSave = getUnsavedEvents(unitOfWork)
        saveEventsInDatabase(eventsToSave)
        publishSavedEvents(eventsToSave)
    }

    private fun getUnsavedEvents(unitOfWork: UnitOfWork): List<EventAndAggregateType> {
        return unitOfWork.domainEntities
            .flatMap { mapToEventAndAggregateType(it) }
            .filter { !it.event.isSaved }
            .toList()
    }


    private fun saveEventsInDatabase(eventsToSave: List<EventAndAggregateType>) {
        jdbcTemplate.batchUpdate(
            INSERT_EVENT_QUERY,
            eventsToSave,
            eventsToSave.size
        ) { preparedStatement, eventAndAggregateType ->
            preparedStatement.setObject(1, eventAndAggregateType.event.aggregateId.rawId)
            preparedStatement.setInt(2, eventAndAggregateType.event.version.rawVersion)
            preparedStatement.setString(3, jsonMapper.write(eventAndAggregateType.event))
            preparedStatement.setString(4, eventAndAggregateType.event::class.qualifiedName)
            preparedStatement.setString(5, eventAndAggregateType.aggregateType.qualifiedName)
        }
    }

    private fun publishSavedEvents(eventsToSave: List<EventAndAggregateType>) {
        for (eventAndAggregateType in eventsToSave)
            eventBus.post(eventAndAggregateType.event)
    }

    private fun mapToEventAndAggregateType(entity: DomainEntity<*>): List<EventAndAggregateType> {
        return entity.history.events()
            .map { EventAndAggregateType(it, entity::class) }
            .toList()
    }
}

data class EventAndAggregateType(val event: Event<*>, val aggregateType: KClass<*>)