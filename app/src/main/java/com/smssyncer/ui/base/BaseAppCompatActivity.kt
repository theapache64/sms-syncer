package com.smssyncer.ui.base

import android.content.DialogInterface
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

abstract class BaseAppCompatActivity : AppCompatActivity() {

    abstract val root: View

    fun snackBar(@StringRes message: Int) {
        snackBar(getString(message))
    }

    fun snackBar(message: String, length: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(root, message, length)
            .show()
    }

    /**
     * Returns a confirm dialog with two button 'YES' and 'CANCEL'.
     */
    fun getOkCancelDialog(@StringRes title: Int, @StringRes message: Int, onOk: () -> Unit): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                onOk()
            }
            .setCancelable(false)
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

}