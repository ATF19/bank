package com.bsfdv.backend.domain.model.core

import java.time.Instant

abstract class Event<TId : Id>(
    val aggregateId: TId, val version: Version,
    val time: Instant, val isForDeletion: Boolean = false
) : Comparable<Event<Id>> {

    override fun compareTo(other: Event<Id>) = version.compareTo(other.version)
}