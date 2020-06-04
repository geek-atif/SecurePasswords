package com.atif.qamar.securepassword.repositories

import com.atif.qamar.securepassword.db.AppDatabase
import com.atif.qamar.securepassword.db.entities.LockScreenPassword
import com.atif.qamar.securepassword.db.entities.PasswordData
import com.atif.qamar.securepassword.preferences.PreferenceProvider

/**
 * Created by Atif Qamar on 27-05-2020.
 */
class AppRepository(private val db: AppDatabase, private val pref: PreferenceProvider) {

    val passwordData = db.getPasswordDatas().getPasswordDatas()

    suspend fun insert(passwordData: PasswordData): Long {
        return db.getPasswordDatas().addPasswordData(passwordData)
    }

    suspend fun delete(passwordData: PasswordData): Int {
        return db.getPasswordDatas().deletePasswordData(passwordData)
    }

    suspend fun update(passwordData: PasswordData): Int {
        return db.getPasswordDatas().updatePasswordData(passwordData)
    }

    suspend fun insertLockScreen(lockScreenPassword: LockScreenPassword): Long {
        return db.getLockScreenPassword().addLockScreenPassword(lockScreenPassword)
    }

    fun savePasswordHashInPref(password: String) {
        pref.savePasswordHash(password)
    }

    suspend fun getPasswordHashForLogin(): String {
        return db.getLockScreenPassword().getLockScreenPassword().password.toString()
    }

    fun getPasswordHashFromPref(): String {
        return pref.getPasswordHash().toString()
    }

    suspend fun updatePasswordHashForLogin(lockScreenPassword: LockScreenPassword): Int {
        return db.getLockScreenPassword().updateLockScreenPassword(lockScreenPassword)
    }

}