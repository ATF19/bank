package com.bsfdv.backend.domain.model.core

import java.time.Instant
import java.util.*

open class EventStream<TId : Id>(private val events: SortedSet<Event<TId>>) {

    constructor(event: Event<TId>) : this(sortedSetOf<Event<TId>>(event))

    fun creationTime(): Instant {
        if (events.isEmpty())
            throw EventStreamCannotBeEmpty()

        return events.first().time
    }

    fun events() = events

    fun id(): TId {
        if (events.isEmpty())
            throw EventStreamCannotBeEmpty()

        return events.first().aggregateId
    }

    fun append(event: Event<TId>) {
        val expectedVersion = events.last().version.next()
        if (expectedVersion != event.version)
            throw InconsistentVersionNumber(expectedVersion, event.version)

        events.add(event)
    }

    fun isEmpty() = events.isEmpty()

}

class EventStreamCannotBeEmpty : DomainException()
class InconsistentVersionNumber(expectedVersion: Version, givenVersion: Version) :
    DomainException("Expected version ${expectedVersion.rawVersion} but instead received ${givenVersion.rawVersion}")