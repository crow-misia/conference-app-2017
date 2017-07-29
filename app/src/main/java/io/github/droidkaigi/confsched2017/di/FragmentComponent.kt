package io.github.droidkaigi.confsched2017.di

import dagger.Subcomponent
import io.github.droidkaigi.confsched2017.di.scope.FragmentScope
import io.github.droidkaigi.confsched2017.view.fragment.ContributorsFragment
import io.github.droidkaigi.confsched2017.view.fragment.InformationFragment
import io.github.droidkaigi.confsched2017.view.fragment.LicensesFragment
import io.github.droidkaigi.confsched2017.view.fragment.MapFragment
import io.github.droidkaigi.confsched2017.view.fragment.MySessionsFragment
import io.github.droidkaigi.confsched2017.view.fragment.SearchFragment
import io.github.droidkaigi.confsched2017.view.fragment.SessionDetailFragment
import io.github.droidkaigi.confsched2017.view.fragment.SessionFeedbackFragment
import io.github.droidkaigi.confsched2017.view.fragment.SessionsFragment
import io.github.droidkaigi.confsched2017.view.fragment.SettingsFragment
import io.github.droidkaigi.confsched2017.view.fragment.SponsorsFragment

@FragmentScope
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

    fun inject(fragment: SessionsFragment)

    fun inject(fragment: MapFragment)

    fun inject(fragment: InformationFragment)

    fun inject(fragment: SettingsFragment)

    fun inject(fragment: SessionDetailFragment)

    fun inject(fragment: SponsorsFragment)

    fun inject(fragment: ContributorsFragment)

    fun inject(fragment: LicensesFragment)

    fun inject(fragment: SessionFeedbackFragment)

    fun inject(fragment: SearchFragment)

    fun inject(fragment: MySessionsFragment)

}
