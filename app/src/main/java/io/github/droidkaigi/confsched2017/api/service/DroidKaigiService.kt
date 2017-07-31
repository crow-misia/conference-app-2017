package io.github.droidkaigi.confsched2017.api.service

import io.github.droidkaigi.confsched2017.model.Session
import io.reactivex.Single
import retrofit2.http.GET

interface DroidKaigiService {

    @GET("sessions.json")
    fun sessionsJa(): Single<List<Session>>

    @GET("en/sessions.json")
    fun sessionsEn(): Single<List<Session>>

}
