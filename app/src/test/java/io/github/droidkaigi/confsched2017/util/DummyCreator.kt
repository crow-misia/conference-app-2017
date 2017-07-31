package io.github.droidkaigi.confsched2017.util

import io.github.droidkaigi.confsched2017.model.*
import java.sql.Date

object DummyCreator {
    fun newMySession(seed: Int) = MySession(seed, newSession(seed))

    fun newSession(seed: Int): Session {
        val seedString = seed.toString()
        val seedLong = seed.toLong()

        return Session(
            id = seed,
            title = seedString,
            desc = seedString,
            speaker = newSpeaker(seed),
            stime = Date(seedLong),
            etime = Date(seedLong),
            durationMin = seed,
            type = seedString,
            topic = newTopic(seed),
            room = newRoom(seed),
            lang = seedString,
            slideUrl = seedString,
            movieUrl = seedString,
            movieDashUrl = seedString,
            shareUrl = seedString)
    }

    fun newSpeaker(seed: Int): Speaker {
        val seedString = seed.toString()

        return Speaker(
            id = seed,
            name = seedString,
            imageUrl = seedString,
            twitterName = seedString,
            githubName = seedString)
    }

    fun newTopic(seed: Int): Topic {
        val seedString = seed.toString()

        return Topic(
            id = seed,
            name = seedString,
            other = seedString)
    }

    fun newRoom(seed: Int) = Room(id = seed, name = seed.toString())

    fun newContributor(seed: Int): Contributor {
        val seedString = seed.toString()

        return Contributor(
            name = seedString,
            avatarUrl = seedString,
            htmlUrl = seedString,
            contributions = seed)
    }
}
