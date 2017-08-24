package io.github.droidkaigi.confsched2017.viewmodel

import io.github.droidkaigi.confsched2017.util.DummyCreator
import org.junit.Test

import org.assertj.core.api.Assertions.assertThat

class TopicColorTest {

    @Test
    fun from() {

        // topic is null
        run {
            assertThat(TopicColor.from(null)).isEqualTo(TopicColor.NONE)
        }

        // invalid topic id
        run {
            assertThat(TopicColor.from(DummyCreator.newTopic(-1))).isEqualTo(TopicColor.NONE)
            assertThat(TopicColor.from(DummyCreator.newTopic(TopicColor.values().size))).isEqualTo(TopicColor.NONE)
        }

        // valid topic id
        // TODO There is not the specification document about topic id yet.
        // So, now just see raw json data. https://droidkaigi.github.io/2017/sessions.json
        run {
            assertThat(TopicColor.from(DummyCreator.newTopic(0)))
                    .isEqualTo(TopicColor.NONE)

            assertThat(TopicColor.from(DummyCreator.newTopic(1)))
                    .isEqualTo(TopicColor.PRODUCTIVITY_AND_TOOLING)

            assertThat(TopicColor.from(DummyCreator.newTopic(2)))
                    .isEqualTo(TopicColor.ARCHITECTURE_AND_DEVELOPMENT_PROCESS_METHODOLOGY)

            assertThat(TopicColor.from(DummyCreator.newTopic(3)))
                    .isEqualTo(TopicColor.HARDWARE)

            assertThat(TopicColor.from(DummyCreator.newTopic(4)))
                    .isEqualTo(TopicColor.UI_AND_DESIGN)

            assertThat(TopicColor.from(DummyCreator.newTopic(5)))
                    .isEqualTo(TopicColor.QUALITY_AND_SUSTAINABILITY)

            assertThat(TopicColor.from(DummyCreator.newTopic(6)))
                    .isEqualTo(TopicColor.PLATFORM)

            assertThat(TopicColor.from(DummyCreator.newTopic(7)))
                    .isEqualTo(TopicColor.OTHER)
        }

    }
}
