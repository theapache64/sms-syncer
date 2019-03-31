package com.smssyncer

import android.app.Application
import com.theah64.safemail.SafeMail

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        SafeMail.init("9dsyI89Pty")
    }
}