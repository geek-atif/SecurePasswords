package com.atif.qamar.securepassword.extensions

import android.content.Context
import android.view.Gravity
import android.widget.Toast

/**
 * Created by Atif Qamar on 29-05-2020.
 */


fun Context.toast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}