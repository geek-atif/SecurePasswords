package com.atif.qamar.securepassword.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.atif.qamar.securepassword.util.Constant

/**
 * Created by Atif Qamar on 02-06-2020.
 */

class PreferenceProvider(context: Context) {
    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun savePasswordHash(passwordHash: String) {
        preference.edit().putString(
            Constant.PASSWORD_HASH, passwordHash
        ).apply()
    }

    fun getPasswordHash(): String? {
        return preference.getString(Constant.PASSWORD_HASH, "")
    }
}