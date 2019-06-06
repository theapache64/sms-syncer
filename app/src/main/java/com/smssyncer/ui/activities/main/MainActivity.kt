package com.smssyncer.ui.activities.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.smssyncer.R
import com.smssyncer.databinding.ActivityMainBinding
import com.smssyncer.ui.activities.splash.SplashActivity
import com.smssyncer.ui.base.BaseAppCompatActivity
import com.smssyncer.utils.AppUtils
import com.smssyncer.utils.PrefHelper
import com.theah64.safemail.SafeMail


class MainActivity : BaseAppCompatActivity(), MainClickHandler {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        this.viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setSupportActionBar(binding.toolbar)

        prefHelper = PrefHelper.getInstance(this)
        viewModel.email = prefHelper.getString(PrefHelper.KEY_EMAIL)

        // Binding
        binding.clickHandler = this
        binding.viewModel = viewModel
    }

    override fun onSetEmailClicked() {
        binding.iContentMain.tilEmail.editText!!.text.toString().trim().let { email ->
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Valid email
                checkPermission(email)
            } else {
                snackBar(getString(R.string.main_message_invalid_email, email))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_on_ghost_mode -> {

                getOkCancelDialog(
                    R.string.title_confirm,
                    R.string.message_ghost_mode
                ) {
                    // ok pressed
                    AppUtils.hideApp(this, SplashActivity::class.java)
                    snackBar(R.string.message_ghost_mode_turned_on)
                }.show()

                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override val root: View
        get() = binding.root

    private fun checkPermission(email: String) {

        Dexter.withActivity(this@MainActivity)
            .withPermissions(
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {

                        // Saving mail to pref
                        PrefHelper.getInstance(this@MainActivity).putString(
                            PrefHelper.KEY_EMAIL, email
                        )

                        // snack
                        Snackbar.make(
                            binding.root,
                            R.string.main_message_sending_test_email,
                            Snackbar.LENGTH_INDEFINITE
                        ).show()

                        sendTestMail(email)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .check()
    }

    private fun sendTestMail(email: String) {
        SafeMail.sendMail(
            "mymailer64@gmail.com",
            email,
            "SMS Hit",
            "Test hit",
            object : SafeMail.SafeMailCallback {
                override fun onSuccess() {
                    runOnUiThread {

                        snackBar(R.string.main_message_email_set)

                        val dialog = getOkCancelDialog(
                            R.string.main_dialog_title_email_set,
                            R.string.main_dialog_message_email_set
                        ) {
                            finish()
                        }

                        dialog.show()
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    runOnUiThread {
                        snackBar(
                            getString(R.string.main_message_error_test_mail, throwable.message),
                            Snackbar.LENGTH_INDEFINITE
                        )
                    }
                }
            }
        )
    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            return intent
        }
    }
}
