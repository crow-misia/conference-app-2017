package io.github.droidkaigi.confsched2017.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

import io.github.droidkaigi.confsched2017.databinding.FragmentSplashBinding

/**
 * Show splash screen, responsible for handling ParticleView animation.
 */
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        scheduledExecutorService.scheduleAtFixedRate(
                { binding.particleAnimationView.postInvalidate() },
                0L,
                40L,
                TimeUnit.MILLISECONDS)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scheduledExecutorService.shutdown()
    }

    companion object {
        val TAG: String = SplashFragment::class.java.simpleName
    }
}
