package com.bsfdv.backend.domain.testUtils

import com.bsfdv.backend.domain.model.core.*
import java.time.Instant
import java.util.*

class TestId : Id {
    constructor() : super()
    constructor(rawId: UUID) : super(rawId)
    constructor(rawId: String) : super(rawId)
}

class TestEvent(
    aggregateId: TestId, version: Version,
    time: Instant, isForDeletion: Boolean, val testName: TestName
) : Event<TestId>(aggregateId, version, time, isForDeletion) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TestEvent) return false

        if (testName != other.testName) return false

        return true
    }

    override fun hashCode(): Int {
        return testName.hashCode()
    }
}

data class TestName(val name: String)

class TestEntity(history: EventStream<TestId>) : DomainEntity<TestId>(history) {

    private lateinit var testName: TestName private set

    companion object {
        fun create(testName: TestName): TestEntity {
            val createdEvent = TestEvent(TestId(), START_VERSION, Instant.now(), false, testName)
            val eventStream = EventStream(createdEvent)
            return TestEntity(eventStream)
        }
    }

    override fun apply(event: Event<TestId>) {
        if (event is TestEvent)
            testName = event.testName
    }

}