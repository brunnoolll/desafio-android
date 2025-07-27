package com.picpay.desafio.android.data.logger

import android.util.Log
import com.picpay.desafio.android.domain.common.Logger
import javax.inject.Inject

class AndroidLogger @Inject constructor() : Logger {

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}