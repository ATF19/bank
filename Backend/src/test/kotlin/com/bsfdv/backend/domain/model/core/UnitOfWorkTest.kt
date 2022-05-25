package com.bsfdv.backend.domain.model.core

import com.bsfdv.backend.domain.testUtils.TestEntity
import com.bsfdv.backend.domain.testUtils.TestName
import com.bsfdv.backend.suites.UNIT_TEST
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag(UNIT_TEST)
internal class UnitOfWorkTest {

    private lateinit var entity: TestEntity

    @BeforeEach
    fun setup() {
        entity = TestEntity.create(TestName("Test for unit of work"))
    }

    @Test
    fun can_create_unit_of_work_for_entity() {
        // given

        // when
        val unitOfWork = UnitOfWork(entity)

        // then
        assertThat(unitOfWork.domainEntities).containsExactly(entity)
    }

    @Test
    fun can_register_an_entity() {
        // given
        val entity2 = TestEntity.create(TestName("Another test for unit of work"))
        val unitOfWork = UnitOfWork(entity)

        // when
        unitOfWork.register(entity2)

        // then
        assertThat(unitOfWork.domainEntities).containsExactlyInAnyOrder(entity, entity2)
    }
}