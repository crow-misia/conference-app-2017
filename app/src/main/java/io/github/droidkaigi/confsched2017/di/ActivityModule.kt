package io.github.droidkaigi.confsched2017.di

import android.support.v7.app.AppCompatActivity

import dagger.Module
import dagger.Provides

@Module
class ActivityModule(internal val activity: AppCompatActivity) {

    @Provides
    fun activity() = activity
}
