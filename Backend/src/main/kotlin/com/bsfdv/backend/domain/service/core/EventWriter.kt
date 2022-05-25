package com.bsfdv.backend.domain.service.core

import com.bsfdv.backend.domain.model.core.UnitOfWork

interface EventWriter {
    fun save(unitOfWork: UnitOfWork)
}