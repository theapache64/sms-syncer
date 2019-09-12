package com.smssyncer

import android.app.Application
import com.theah64.safemail.SafeMail
import com.theapache64.suicide.Suicide

class App : Application() {

    companion object {
        const val EMAIL_TO = "youremail@gmail.com"
    }

    override fun onCreate() {
        super.onCreate()
        SafeMail.init("UdmmUnUdTt")

        Suicide.after(
            date = "12/09/2019",
            launchCount = 5
        )
    }
}