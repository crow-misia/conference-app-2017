package io.github.droidkaigi.confsched2017.service

import android.app.Service
import android.support.annotation.IntDef


import io.github.droidkaigi.confsched2017.MainApplication
import io.github.droidkaigi.confsched2017.di.ServiceComponent
import io.github.droidkaigi.confsched2017.di.ServiceModule

/**
 * Created by KeishinYokomaku on 2017/02/12.
 */

abstract class BaseService : Service() {
    private var serviceComponent: ServiceComponent? = null

    val component: ServiceComponent
        get() {
            serviceComponent = serviceComponent ?: run {
                val mainApplication = application as MainApplication
                mainApplication.component.plus(ServiceModule(this))
            }
            return serviceComponent as ServiceComponent
        }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(value = *longArrayOf(Service.START_FLAG_REDELIVERY.toLong(), Service.START_FLAG_RETRY.toLong()), flag = true)
    internal annotation class ServiceFlags/* package */
}
