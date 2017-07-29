package io.github.droidkaigi.confsched2017.view.fragment

import android.support.v4.app.Fragment

import io.github.droidkaigi.confsched2017.di.FragmentComponent
import io.github.droidkaigi.confsched2017.di.FragmentModule
import io.github.droidkaigi.confsched2017.view.activity.BaseActivity

abstract class BaseFragment : Fragment() {

    private var fragmentComponent: FragmentComponent? = null

    val component: FragmentComponent
        get() = fragmentComponent ?: run {
            when (activity) {
                is BaseActivity -> {
                    val component = (activity as BaseActivity).component.plus(FragmentModule(this))
                    fragmentComponent = component
                    return component
                }
                else -> throw IllegalStateException("The activity of this fragment is not an instance of BaseActivity")
            }
        }
}
