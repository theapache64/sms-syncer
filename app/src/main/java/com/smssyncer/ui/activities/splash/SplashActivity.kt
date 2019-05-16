package com.smssyncer.ui.activities.splash

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.smssyncer.BuildConfig
import com.smssyncer.R
import com.smssyncer.databinding.ActivitySplashBinding
import com.smssyncer.ui.activities.main.MainActivity
import com.smssyncer.utils.PrefHelper


class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_DURATION_IN_MILLIS = 1500L
        private const val POLICY_URL = "https://github.com/theapache64/sms-syncer/blob/master/extras/privacy_policy.md"
    }

    private lateinit var prefHelper: PrefHelper
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)

        this.viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        viewModel.versionNumber = "v${BuildConfig.VERSION_NAME}"

        prefHelper = PrefHelper.getInstance(this)

        // Binding ViewModel
        binding.viewModel = viewModel

        Handler().postDelayed({
            doSplashWork()
        }, SPLASH_DURATION_IN_MILLIS)
    }

    private fun doSplashWork() {

        val isTermsAccepted = prefHelper.getBoolean(PrefHelper.KEY_IS_TERMS_ACCEPTED)
        if (isTermsAccepted) {
            moveToMain()
        } else {
            val termsDialog = getPrivacyDialog()
            termsDialog.show()
        }
    }


    private fun getPrivacyDialog(): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle(R.string.splash_dialog_title_privacy_policy)
            .setMessage(R.string.splash_dialog_message_privacy_policy)
            .setCancelable(false)
            .setPositiveButton(R.string.action_accept) { _: DialogInterface, _: Int ->
                // accepted
                prefHelper.putBoolean(PrefHelper.KEY_IS_TERMS_ACCEPTED, true)
                moveToMain()
            }
            .setNegativeButton(R.string.action_decline) { _: DialogInterface, _: Int ->
                Toast.makeText(this, R.string.splash_toast_terms_declined, Toast.LENGTH_SHORT).show();
                finish()
            }
            .setNeutralButton(R.string.action_view_policy) { _: DialogInterface, _: Int ->
                openPolicy()
                viewModel.isShowPrivacyPolicyOnResume = true
            }
            .create()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isShowPrivacyPolicyOnResume) {
            val dialog = getPrivacyDialog()
            dialog.show()
            viewModel.isShowPrivacyPolicyOnResume = false
        }
    }


    private fun openPolicy() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(POLICY_URL)))
    }

    private fun moveToMain() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }


}
