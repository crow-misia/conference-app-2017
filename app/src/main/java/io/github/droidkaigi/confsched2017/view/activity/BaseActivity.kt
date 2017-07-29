package io.github.droidkaigi.confsched2017.view.activity

import com.tomoima.debot.Debot

import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem

import io.github.droidkaigi.confsched2017.MainApplication
import io.github.droidkaigi.confsched2017.di.ActivityComponent
import io.github.droidkaigi.confsched2017.di.ActivityModule
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

abstract class BaseActivity : AppCompatActivity() {

    private var activityComponent: ActivityComponent? = null

    private lateinit var debot: Debot

    val component: ActivityComponent
        get() = activityComponent ?: run {
            val mainApplication = application as MainApplication
            val component = mainApplication.component.plus(ActivityModule(this))
            activityComponent = component
            return component
        }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        debot = Debot.getInstance()
        debot.allowShake(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        debot.startSensor(this)
    }

    override fun onPause() {
        super.onPause()
        debot.stopSensor()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            debot.showDebugMenu(this)
        }
        return super.onKeyUp(keyCode, event)
    }

    internal fun replaceFragment(fragment: Fragment, @IdRes @LayoutRes layoutResId: Int) {
        supportFragmentManager.beginTransaction()
                .replace(layoutResId, fragment, fragment.javaClass.simpleName)
                .commit()
    }

    internal fun initBackToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = toolbar.title
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}
