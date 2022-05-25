package com.bsfdv.backend.domain.service.core

import com.bsfdv.backend.domain.model.core.DomainEntity
import com.bsfdv.backend.domain.model.core.EventStream
import com.bsfdv.backend.domain.model.core.Id
import kotlin.reflect.KClass

interface EventReader {
    fun <TId : Id> by(id: TId): EventStream<TId>
    fun <TId : Id, TE : DomainEntity<TId>> byType(aggregateType: KClass<TE>): List<EventStream<TId>>
}