package io.github.droidkaigi.confsched2017.di

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

import dagger.Module
import dagger.Provides

@Module
class FragmentModule(internal val fragment: Fragment) {

    @Provides
    fun provideFragmentManager(): FragmentManager = fragment.fragmentManager
}
