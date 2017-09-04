package io.github.droidkaigi.confsched2017.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit

inline fun <reified T : Any> Retrofit.create(): T = this.create(T::class.java)

inline fun <reified T : Any> Gson.fromJson(json: String): T {
    val listType = object : TypeToken<T>() { }.type
    return this.fromJson(json, listType)
}
