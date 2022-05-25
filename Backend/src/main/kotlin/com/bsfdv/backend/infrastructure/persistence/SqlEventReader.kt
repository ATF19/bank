package com.bsfdv.backend.infrastructure.persistence

import com.bsfdv.backend.domain.model.core.DomainEntity
import com.bsfdv.backend.domain.model.core.Event
import com.bsfdv.backend.domain.model.core.EventStream
import com.bsfdv.backend.domain.model.core.Id
import com.bsfdv.backend.domain.service.core.EventReader
import com.bsfdv.backend.infrastructure.mapping.JsonMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import kotlin.reflect.KClass

@Repository
class SqlEventReader(private val jdbcTemplate: JdbcTemplate, private val jsonMapper: JsonMapper) : EventReader {

    override fun <TId : Id> by(id: TId): EventStream<TId> {
        val rows = jdbcTemplate.queryForList(SELECT_EVENTS_BY_ID_QUERY, id.rawId)
        return mapToEventStream(rows)
    }

    override fun <TId : Id, TE : DomainEntity<TId>> byType(aggregateType: KClass<TE>): List<EventStream<TId>> {
        val rows = jdbcTemplate.queryForList(SELECT_EVENTS_BY_TYPE_QUERY, aggregateType.qualifiedName)
        return mapToListOfEventStreams(rows)
    }

    private fun <TId : Id> mapToEventStream(rows: List<Map<String, Any>>): EventStream<TId> {
        val events = rows.map { mapToEvent(it) as Event<TId> }
            .toSortedSet(compareBy { it })
        return PersistenceAwareEventStream(events)
    }

    private fun <TId : Id> mapToListOfEventStreams(rows: List<Map<String, Any>>): List<EventStream<TId>> {
        return rows.map { mapToEvent(it) }
            .groupBy { it.aggregateId }
            .map { PersistenceAwareEventStream(it.value as List<Event<TId>>) }
    }

    private fun mapToEvent(row: Map<String, Any>): Event<*> {
        return jsonMapper.readFromString(
            String(row["PAYLOAD"] as ByteArray),
            row["EVENT_TYPE"] as String
        )
    }
}