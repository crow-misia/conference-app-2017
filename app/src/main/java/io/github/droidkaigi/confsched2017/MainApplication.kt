package io.github.droidkaigi.confsched2017

import com.deploygate.sdk.DeployGate
import com.squareup.leakcanary.LeakCanary
import com.tomoima.debot.DebotConfigurator
import com.tomoima.debot.DebotStrategyBuilder

import android.app.Application
import android.content.Intent
import android.os.Build

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.debug.ClearCache
import io.github.droidkaigi.confsched2017.debug.NotificationStrategy
import io.github.droidkaigi.confsched2017.debug.ShowSplashStrategy
import io.github.droidkaigi.confsched2017.di.AndroidModule
import io.github.droidkaigi.confsched2017.di.AppComponent
import io.github.droidkaigi.confsched2017.di.AppModule
import io.github.droidkaigi.confsched2017.di.DaggerAppComponent
import io.github.droidkaigi.confsched2017.log.CrashLogTree
import io.github.droidkaigi.confsched2017.log.LogEmitter
import io.github.droidkaigi.confsched2017.log.OverlayLogTree
import io.github.droidkaigi.confsched2017.pref.DefaultPrefs
import io.github.droidkaigi.confsched2017.service.DebugOverlayService
import io.github.droidkaigi.confsched2017.util.AppShortcutsUtil
import io.github.droidkaigi.confsched2017.util.LocaleUtil
import io.github.droidkaigi.confsched2017.util.intentFor
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

open class MainApplication : Application() {

    lateinit var component: AppComponent

    @Inject
    lateinit var clearCache: ClearCache

    @Inject
    lateinit var notificationStrategy: NotificationStrategy

    @Inject
    lateinit var showSplashStrategy: ShowSplashStrategy

    @Inject
    lateinit var defaultPrefs: DefaultPrefs

    @Inject
    lateinit var emitter: LogEmitter

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .androidModule(AndroidModule(this))
                .build()
        component.inject(this)
        initCalligraphy()
        initLeakCanary()
        initAppShortcuts()

        if (!DeployGate.isInitialized()) {
            DeployGate.install(this, null, true)
        }
        Timber.plant(CrashLogTree())
        Timber.plant(OverlayLogTree(emitter))
        LocaleUtil.initLocale(this)

        if (defaultPrefs.showDebugOverlayView) {
            startService(intentFor<DebugOverlayService>())
        }
        initDebot()
    }

    private fun initCalligraphy() {
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_noto_cjk_medium))
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    fun initDebot() {
        val notificationTestTitle: String
        if (defaultPrefs.notificationTestFlag) {
            notificationTestTitle = "Notification test OFF"
        } else {
            notificationTestTitle = "Notification test ON"
        }
        val builder = DebotStrategyBuilder.Builder(this)
                .registerMenu("Clear cache", clearCache)
                .registerMenu(notificationTestTitle, notificationStrategy)
                .registerMenu("Show splash view", showSplashStrategy)
                .build()
        DebotConfigurator.configureWithCustomizedMenu(this, builder.strategyList)
    }

    private fun initAppShortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            AppShortcutsUtil.addShortcuts(this)
        }
    }
}
