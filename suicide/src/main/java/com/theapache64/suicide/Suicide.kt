package com.theapache64.suicide

import android.content.Context

object Suicide {

    private const val KEY_SHARED_PREF_NAME = "suicide"
    private var launchCount: Int? = null
    private var date: String? = null

    /**
     *
     */
    fun after(context: Context, date: String? = null, launchCount: Int? = null) {
        require(date != null || launchCount != null) { "Either date or launchCount must be defined" }

        this.date = date
        this.launchCount = launchCount

        // Incrementing launch count
        val sharedPref = context.getSharedPreferences(KEY_SHARED_PREF_NAME)
        val currentLaunchCount = getCurrentLaunchCode()
    }


}