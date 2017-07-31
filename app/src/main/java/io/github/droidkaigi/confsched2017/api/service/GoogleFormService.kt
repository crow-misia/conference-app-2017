package io.github.droidkaigi.confsched2017.api.service

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GoogleFormService {

    @POST("e/1FAIpQLSf5NydpYm48GXqlKqbG3e0dna3bw5HJ4GUg8W1Yfe4znTWH_g/formResponse")
    @FormUrlEncoded
    fun submitSessionFeedback(
            @Field("entry.1298546024") sessionId: Int,
            @Field("entry.413792998") sessionTitle: String,
            @Field("entry.335146475") relevancy: Int,
            @Field("entry.1916895481") asExpected: Int,
            @Field("entry.1501292277") difficulty: Int,
            @Field("entry.2121897737") knowledgeable: Int,
            @Field("entry.645604473") comment: String?): Single<Response<Void>>

}
