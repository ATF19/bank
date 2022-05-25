package com.bsfdv.backend.domain.model.core

import com.bsfdv.backend.domain.testUtils.TestEvent
import com.bsfdv.backend.domain.testUtils.TestId
import com.bsfdv.backend.domain.testUtils.TestName
import com.bsfdv.backend.suites.UNIT_TEST
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Instant

@Tag(UNIT_TEST)
internal class EventStreamTest {

    private lateinit var id: TestId
    private lateinit var event1: Event<TestId>
    private lateinit var event2: Event<TestId>
    private lateinit var event3: Event<TestId>
    private lateinit var eventStream: EventStream<TestId>

    @BeforeEach
    fun setup() {
        id = TestId()
        val name = TestName("A test name")
        event1 = TestEvent(id, START_VERSION, Instant.parse("2022-05-23T10:15:30.00Z"), false, name)
        event2 = TestEvent(id, event1.version.next(), Instant.parse("2022-05-23T10:16:30.00Z"), false, name)
        event3 = TestEvent(id, event2.version.next(), Instant.parse("2022-05-23T10:17:30.00Z"), true, name)
        eventStream = EventStream<TestId>(sortedSetOf(event3, event1, event2))
    }

    @Test
    fun throw_error_when_trying_to_get_creation_time_for_empty_event_stream() {
        // given
        val eventStream = EventStream<TestId>(sortedSetOf<Event<TestId>>())

        // when

        // then
        assertThatThrownBy { eventStream.creationTime() }.isInstanceOf(EventStreamCannotBeEmpty::class.java)
    }

    @Test
    fun get_event_stream_creation_time() {
        // given

        // when
        val time = eventStream.creationTime()

        // then
        assertThat(time).isEqualTo(event1.time)
    }

    @Test
    fun throw_error_when_trying_to_get_id_for_empty_event_stream() {
        // given
        val eventStream = EventStream<TestId>(sortedSetOf<Event<TestId>>())

        // when

        // then
        assertThatThrownBy { eventStream.id() }.isInstanceOf(EventStreamCannotBeEmpty::class.java)
    }

    @Test
    fun get_event_stream_id() {
        // given

        // when
        val result = eventStream.id()

        // then
        assertThat(result).isEqualTo(id)
    }

    @Test
    fun get_all_events() {
        // given

        // when
        val result = eventStream.events()

        // then
        assertThat(result).containsExactly(event1, event2, event3)
    }

    @Test
    fun throw_error_when_appending_event_with_wrong_version() {
        // given
        val event4 =
            TestEvent(id, Version(100), Instant.parse("2022-05-23T10:20:30.00Z"), false, TestName("A test entity"))

        // when

        // then
        assertThatThrownBy { eventStream.append(event4) }.isInstanceOf(InconsistentVersionNumber::class.java)
    }

    @Test
    fun append_event() {
        // given
        val event4 = TestEvent(
            id,
            event3.version.next(),
            Instant.parse("2022-05-23T10:20:30.00Z"),
            false,
            TestName("A test entity")
        )

        // when
        eventStream.append(event4)

        // then
        assertThat(eventStream.events()).containsExactly(event1, event2, event3, event4)
    }
}