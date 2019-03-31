package com.smssyncer.ui.activities.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.smssyncer.BuildConfig
import com.smssyncer.R
import com.smssyncer.databinding.ActivitySplashBinding
import com.smssyncer.ui.activities.main.MainActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)

        val viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        viewModel.versionNumber = "v${BuildConfig.VERSION_NAME}"

        // Binding ViewModel
        binding.viewModel = viewModel

        Handler().postDelayed({
            startActivity(MainActivity.getStartIntent(this))
            finish()
        }, SPLASH_DURATION_IN_MILLIS)
    }

    companion object {
        private const val SPLASH_DURATION_IN_MILLIS = 1500L
    }

}
