package com.bsfdv.backend.infrastructure.persistence

import com.bsfdv.backend.domain.model.core.Event
import com.bsfdv.backend.domain.model.core.EventStream
import com.bsfdv.backend.domain.model.core.Id
import com.bsfdv.backend.domain.model.core.Version
import java.util.*

class PersistenceAwareEventStream<TId : Id> : EventStream<TId> {

    private val lastSavedVersion: Version

    constructor(events: SortedSet<Event<TId>>) : super(events) {
        lastSavedVersion = if (events.isEmpty()) Version(-1) else events.last().version
    }

    constructor(events: List<Event<TId>>) : this(events.toSortedSet(compareBy { it }))

    fun unsavedEvents(): SortedSet<Event<TId>> {
        return events()
            .filter { it.version.isAfter(lastSavedVersion) }
            .toSortedSet(compareBy { it })
    }
}