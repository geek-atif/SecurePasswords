package com.atif.qamar.securepassword.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atif.qamar.securepassword.db.entities.LockScreenPassword
import com.atif.qamar.securepassword.repositories.AppRepository
import com.atif.qamar.securepassword.util.Event
import com.atif.qamar.securepassword.util.Utility
import kotlinx.coroutines.launch

@SuppressLint("LongLogTag")
class LockScreenPasswordViewModel(private val appRepository: AppRepository) : ViewModel() {

    val TAG = "com.atif.qamar.securepassword.viewmodel.LockScreenPasswordViewModel"
    var password: String? = null
    var rePassword: String? = null
    var row = 0


    private val _rowId = MutableLiveData<Long>()
    val rowId: LiveData<Long>
        get() = _rowId

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        _rowId.value = -1
        row = 0
    }

    fun savePasswordDetail() {

        if (password.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter password")
            return
        }

        if (rePassword.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter Re-enter password")
            return
        }

        if (password!!.length != 6) {
            statusMessage.value = Event("Password length should be 6")
            return
        }

        if (!password.equals(rePassword)) {
            statusMessage.value = Event("Please enter same password")
            return
        }

        insert(LockScreenPassword(row, Utility.generateHash(password.toString().trim())))
    }

    private fun insert(lockScreenPassword: LockScreenPassword) = viewModelScope.launch {
        val newRowId = appRepository.insertLockScreen(lockScreenPassword)
        if (newRowId > -1) {
            appRepository.savePasswordHashInPref(lockScreenPassword.password.toString())
            _rowId.value = 1
        } else
            _rowId.value = 0L
    }

}



