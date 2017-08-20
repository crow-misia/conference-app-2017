package io.github.droidkaigi.confsched2017.view.fragment

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.fragmentargs.FragmentArgs
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.FragmentSessionDetailBinding
import io.github.droidkaigi.confsched2017.view.helper.AnimationHelper
import io.github.droidkaigi.confsched2017.viewmodel.SessionDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@FragmentWithArgs
class SessionDetailFragment : BaseFragment(), SessionDetailViewModel.Callback {

    @Inject
    lateinit var viewModel: SessionDetailViewModel

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Arg
    var sessionId: Int = 0

    private lateinit var binding: FragmentSessionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FragmentArgs.inject(this)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

    private fun initTheme(activity: Activity?) {
        activity ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change theme by topic
            activity.setTheme(viewModel.topicThemeResId)

            val taskDescription = ActivityManager.TaskDescription(viewModel.sessionTitle, null,
                    ContextCompat.getColor(activity, viewModel.sessionVividColorResId))
            activity.setTaskDescription(taskDescription)

            // Change status bar scrim color
            val typedValue = TypedValue()
            activity.theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true)
            val colorPrimaryDark = typedValue.data
            if (colorPrimaryDark != 0) {
                binding.collapsingToolbar.setStatusBarScrimColor(colorPrimaryDark)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val activity = activity
        binding = FragmentSessionDetailBinding.inflate(inflater, container, false)
        viewModel.callback = this
        binding.viewModel = viewModel
        setHasOptionsMenu(true)
        viewModel.loadSession(sessionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            initTheme(activity)
                            binding.viewModel = viewModel
                        },
                        onError = { Timber.tag(TAG).e(it, "Failed to find session.") }
                ).addTo(compositeDisposable)

        initToolbar()
        initScroll()
        return binding.root
    }

    private fun initScroll() {
        binding.nestedScroll.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                binding.fab.hide()
            }
            if (scrollY < oldScrollY) {
                binding.fab.show()
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onDetach() {
        viewModel.destroy()
        super.onDetach()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
                setDisplayShowTitleEnabled(false)
                setHomeButtonEnabled(true)
            }
        }
    }

    override fun onClickFab(selected: Boolean) {
        AnimationHelper.startVDAnimation(binding.fab,
                R.drawable.avd_add_to_check_24dp, R.drawable.avd_check_to_add_24dp,
                resources.getInteger(R.integer.fab_vector_animation_mills))
        val textId: Int
        val actionTextId: Int
        if (selected) {
            textId = R.string.session_checked
            actionTextId = R.string.session_uncheck
        } else {
            textId = R.string.session_unchecked
            actionTextId = R.string.session_check
        }
        val typedValue = TypedValue()
        activity.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        val actionTextColor = typedValue.data
        Snackbar.make(binding.fab, textId, Snackbar.LENGTH_SHORT)
                .setAction(actionTextId) { _ -> binding.fab.performClick() }
                .setActionTextColor(actionTextColor)
                .show()
    }

    override fun onOverScroll() {
        activity.finish()
        activity.overridePendingTransition(0, 0)
    }

    companion object {
        private val TAG = SessionDetailFragment::class.java.simpleName
    }
}
