package com.bsfdv.backend.domain.model.core

data class UnitOfWork(val domainEntities: MutableList<DomainEntity<*>>) {
    constructor() : this(ArrayList())

    constructor(entity: DomainEntity<*>) : this(mutableListOf(entity))

    constructor(vararg entity: DomainEntity<*>) : this(entity.toMutableList())

    fun register(entity: DomainEntity<*>) {
        domainEntities.add(entity)
    }
}