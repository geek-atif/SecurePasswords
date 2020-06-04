package com.atif.qamar.securepassword.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atif.qamar.securepassword.db.entities.LockScreenPassword
import com.atif.qamar.securepassword.repositories.AppRepository
import com.atif.qamar.securepassword.util.Event
import com.atif.qamar.securepassword.util.Utility
import kotlinx.coroutines.launch

class PasswordChangeViewModel(private val appRepository: AppRepository) : ViewModel() {

    var oldPassword: String = ""
    var newPassword: String = ""
    var reNewPasswotrd: String = ""
    var isOldPasswordValid = false
    var isSamePassword = false
    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    private val _rowId = MutableLiveData<Int>()
    val rowId: LiveData<Int>
        get() = _rowId

    init {
        _rowId.value = 0
        isOldPasswordValid = false
        isSamePassword = false
    }

    fun updatePassword() {
        if (oldPassword.isEmpty()) {
            statusMessage.value = Event("Please enter old password")
            return
        }

        if (newPassword.isEmpty()) {
            statusMessage.value = Event("Please enter new password")
            return
        }

        if (reNewPasswotrd.isEmpty()) {
            statusMessage.value = Event("Please enter re-password")
            return
        }

        if (!newPassword.equals(reNewPasswotrd)) {
            statusMessage.value = Event("Both password should be same")
            return
        }

        verifyPasswordHash(oldPassword.toString().trim())
        if (!isOldPasswordValid) {
            statusMessage.value = Event("Wrong old password")
            return
        }

        isSamePassword(newPassword.toString().trim())
        if (isSamePassword) {
            statusMessage.value = Event("Please other password then old password")
            return
        }

        updateNewPassword(LockScreenPassword(1, Utility.generateHash(newPassword)))
    }

    private fun updateNewPassword(lockScreenPassword: LockScreenPassword) = viewModelScope.launch {
        val newRowId = appRepository.updatePasswordHashForLogin(lockScreenPassword)
        if (newRowId > 0) {
            _rowId.value = 1
            appRepository.savePasswordHashInPref(lockScreenPassword.password.toString())
        } else
            _rowId.value = 0
    }

    private fun verifyPasswordHash(userPasswordHash: String) = viewModelScope.launch {
        var passwordHash = appRepository.getPasswordHashFromPref()

        if (passwordHash.isEmpty())
            passwordHash = Utility.generateHash(appRepository.getPasswordHashForLogin())

        if (passwordHash.trim().contentEquals(Utility.generateHash(userPasswordHash).trim()))
            isOldPasswordValid = true
        else
            isOldPasswordValid = false
    }

    private fun isSamePassword(newPassword: String) = viewModelScope.launch {
        var passwordHash = appRepository.getPasswordHashFromPref()

        if (passwordHash.isEmpty())
            passwordHash = Utility.generateHash(appRepository.getPasswordHashForLogin())

        if (passwordHash.trim().contentEquals(Utility.generateHash(newPassword).trim()))
            isSamePassword = true
        else
            isSamePassword = false
    }
}
