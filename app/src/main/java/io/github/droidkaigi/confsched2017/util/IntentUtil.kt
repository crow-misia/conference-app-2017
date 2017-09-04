package io.github.droidkaigi.confsched2017.util

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

inline fun <reified T : Any> Context.intentFor(): Intent = Intent(this, T::class.java)

inline fun <reified T : Any> Fragment.intentFor(): Intent = Intent(activity, T::class.java)
