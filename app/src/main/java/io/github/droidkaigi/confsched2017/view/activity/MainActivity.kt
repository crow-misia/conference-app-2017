package io.github.droidkaigi.confsched2017.view.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.ActivityMainBinding
import io.github.droidkaigi.confsched2017.util.LocaleUtil
import io.github.droidkaigi.confsched2017.view.fragment.InformationFragment
import io.github.droidkaigi.confsched2017.view.fragment.MapFragment
import io.github.droidkaigi.confsched2017.view.fragment.SessionsFragment
import io.github.droidkaigi.confsched2017.view.fragment.SettingsFragment
import io.github.droidkaigi.confsched2017.view.helper.BottomNavigationViewHelper

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var sessionsFragment: Fragment

    private lateinit var mapFragment: Fragment

    private lateinit var informationFragment: Fragment

    private lateinit var settingsFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleUtil.initLocale(this)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        component.inject(this)

        initView()
        initFragments(savedInstanceState)
    }

    private fun initView() {
        BottomNavigationViewHelper.disableShiftingMode(binding.bottomNav)
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            binding.title.text = item.title
            item.isChecked = true
            when (item.itemId) {
                R.id.nav_sessions -> switchFragment(sessionsFragment, SessionsFragment.TAG)
                R.id.nav_map -> switchFragment(mapFragment, MapFragment.TAG)
                R.id.nav_information -> switchFragment(informationFragment, InformationFragment.TAG)
                R.id.nav_settings -> switchFragment(settingsFragment, SettingsFragment.TAG)
            }
            false
        }
    }

    private fun initFragments(savedInstanceState: Bundle?) {
        supportFragmentManager.apply {
            sessionsFragment = findFragmentByTag(SessionsFragment.TAG) ?: SessionsFragment.newInstance()
            mapFragment = findFragmentByTag(MapFragment.TAG) ?: MapFragment.newInstance()
            informationFragment = findFragmentByTag(InformationFragment.TAG) ?: InformationFragment.newInstance()
            settingsFragment = findFragmentByTag(SettingsFragment.TAG) ?: SettingsFragment.newInstance()
        }

        if (savedInstanceState == null) {
            switchFragment(sessionsFragment, SessionsFragment.TAG)
        }
    }

    private fun switchFragment(fragment: Fragment, tag: String): Boolean {
        if (fragment.isAdded) {
            return false
        }

        val manager = supportFragmentManager
        val ft = manager.beginTransaction()

        manager.findFragmentById(R.id.content_view)?.let {
            ft.detach(it)
        }
        if (fragment.isDetached) {
            ft.attach(fragment)
        } else {
            ft.add(R.id.content_view, fragment, tag)
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()

        // NOTE: When this method is called by user's continuous hitting at the same time,
        // transactions are queued, so necessary to reflect commit instantly before next transaction starts.
        manager.executePendingTransactions()

        return true
    }

    override fun onBackPressed() {
        if (switchFragment(sessionsFragment, SessionsFragment.TAG)) {
            binding.bottomNav.menu.findItem(R.id.nav_sessions).isChecked = true
            binding.title.text = getString(R.string.sessions)
            return
        }
        super.onBackPressed()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
