package com.bsfdv.backend.domain.service.core

import com.bsfdv.backend.domain.model.core.DomainException
import com.bsfdv.backend.domain.model.core.Id

class NoSuchEntityExists(entityName: String, id: Id) : DomainException("No such $entityName exists with ID ${id.rawId}")