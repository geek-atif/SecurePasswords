package com.atif.qamar.securepassword.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atif.qamar.securepassword.repositories.AppRepository
import com.atif.qamar.securepassword.util.Event
import com.atif.qamar.securepassword.util.Utility
import kotlinx.coroutines.launch

@SuppressLint("LongLogTag")
class LoginViewModel(private val appRepository: AppRepository) : ViewModel() {

    val TAG = "com.atif.qamar.securepassword.viewmodel.LoginViewModel"
    var password: String? = null


    private val _isPasswordHashValid = MutableLiveData<Boolean>()
    val isPwdHashValid: LiveData<Boolean>
        get() = _isPasswordHashValid

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage


    fun login() {

        if (password.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter password")
            return
        }

        verifyPasswordHash(password.toString().trim())
    }

    private fun verifyPasswordHash(userPasswordHash: String) = viewModelScope.launch {
        var passwordHash = appRepository.getPasswordHashFromPref()

        if (passwordHash.isEmpty())
            passwordHash = Utility.generateHash(appRepository.getPasswordHashForLogin())

        if (passwordHash.trim().contentEquals(Utility.generateHash(userPasswordHash).trim()))
            _isPasswordHashValid.value = true
        else
            _isPasswordHashValid.value = false

    }

    fun isPasswordHashAvailable(): Boolean {
        if (appRepository.getPasswordHashFromPref().isEmpty())
            return false
        return true
    }
}



