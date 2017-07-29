package io.github.droidkaigi.confsched2017.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.di.scope.ActivityScope

@ActivityScope
class ToolbarViewModel @Inject constructor() : BaseObservable(), ViewModel {
    @get:Bindable
    var toolbarTitle: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.toolbarTitle)
        }

    override fun destroy() {
        // Nothing to do
    }
}
