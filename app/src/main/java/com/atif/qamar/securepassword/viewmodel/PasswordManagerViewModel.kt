package com.atif.qamar.securepassword.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atif.qamar.securepassword.db.entities.PasswordData
import com.atif.qamar.securepassword.repositories.AppRepository
import kotlinx.coroutines.launch

@SuppressLint("LongLogTag")
class PasswordManagerViewModel(private val appRepository: AppRepository) : ViewModel() {

    val passwordData = appRepository.passwordData
    var TAG = "com.atif.qamar.securepassword.viewmodel.PasswordManagerViewModel"
    private val _rowId = MutableLiveData<Int>()
    val rowId: LiveData<Int>
        get() = _rowId

    init {
        _rowId.value = -1
    }

    fun delete(passwordData: PasswordData) = viewModelScope.launch {
        val newRowId = appRepository.delete(passwordData)
        Log.i(TAG, "newRowId : $newRowId")
        if (newRowId > 0)
            _rowId.value = 1
        else
            _rowId.value = 0
    }
}
