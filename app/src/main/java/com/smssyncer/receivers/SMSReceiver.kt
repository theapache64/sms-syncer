package com.smssyncer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import com.smssyncer.utils.PrefHelper
import com.theah64.safemail.SafeMail

class SMSReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    }

    override fun onReceive(context: Context, intent: Intent) {

        val action = intent.action
        if (action != null && action == ACTION_SMS_RECEIVED) {

            val bundle = intent.extras
            if (bundle != null) {
                try {
                    val pdus = bundle.get("pdus") as Array<*>
                    val msgs = arrayOfNulls<SmsMessage>(pdus.size)
                    for (i in msgs.indices) {
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        val msgFrom = msgs[i]!!.originatingAddress
                        val msgBody = msgs[i]!!.messageBody
                        val email = PrefHelper.getInstance(context).getString(PrefHelper.KEY_EMAIL)

                        if (email != null) {

                            SafeMail.sendMail("smssyncer64@gmail.com", email,
                                "New SMS : " + msgFrom!!,
                                msgBody,
                                object : SafeMail.SafeMailCallback {
                                    override fun onSuccess() {
                                        Log.i("theapache64", "SMS Sent")
                                    }

                                    override fun onFailed(throwable: Throwable) {
                                        Log.e("theapache64", "SMS not sent")
                                        throwable.printStackTrace()
                                    }
                                }

                            )
                        } else {
                            Log.e("SMSReceiver", "No email defined")
                        }
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }


    }
}
