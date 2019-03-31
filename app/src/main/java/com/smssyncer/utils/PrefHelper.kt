package com.smssyncer.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PrefHelper private constructor(context: Context) {

    private var context: Context? = null
    private val sharedPref: SharedPreferences

    init {
        this.context = context.applicationContext
        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    }

    private fun setContext(context: Context) {
        this.context = context.applicationContext
    }

    internal fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }


    internal fun putString(key: String, value: String) {
        sharedPref.edit()
            .putString(key, value)
            .apply()
    }

    companion object {

        val KEY_EMAIL = "email"

        @SuppressLint("StaticFieldLeak")
        private var instance: PrefHelper? = null

        internal fun getInstance(context: Context): PrefHelper {

            if (instance == null) {
                instance = PrefHelper(context)
            } else {
                instance!!.setContext(context)
            }

            return instance!!
        }
    }
}
