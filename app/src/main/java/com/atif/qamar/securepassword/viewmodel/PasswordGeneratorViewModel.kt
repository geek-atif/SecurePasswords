package com.atif.qamar.securepassword.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.atif.qamar.securepassword.util.Event
import com.atif.qamar.securepassword.util.Utility


@SuppressLint("LongLogTag")
class PasswordGeneratorViewModel() : ViewModel() {
    val TAG = "com.atif.qamar.securepassword.viewmodel.PasswordGeneratorViewModel"

    var isLowerCaseRequired = false
    var isUpperCaseRequired = false
    var isDigitRequired = false
    var isSpecialCharRequired = false
    val progressLiveData = MutableLiveData("8")

    private val passwordLiveData = MutableLiveData<String>()
    val password: LiveData<String>
        get() = passwordLiveData

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        isLowerCaseRequired = false
        isUpperCaseRequired = false
        isDigitRequired = false
        isSpecialCharRequired = false
    }

    fun updateEditText(percentage: Int) {
        Log.i(TAG, "percentage ${percentage}")
        progressLiveData.value = percentage.toString()
    }

    fun onEditTextTyped(): LiveData<Int> {
        return Transformations.switchMap(progressLiveData, {
            val liveData = MutableLiveData<Int>()

            try {
                Log.i(TAG, "onEditTextTyped() ${it.toString().toInt()}")
                liveData.value = it.toString().toInt()
            } catch (e: NumberFormatException) {
                // reset the progress bar if the progress text is invalid
                liveData.value = 8
            }
            liveData
        })
    }

    fun generatePassword() {

        if (isUpperCaseRequired || isLowerCaseRequired || isSpecialCharRequired || isDigitRequired) {

            if (progressLiveData.value!!.toInt() < 8) {
                statusMessage.value = Event("Minimum length should be 8")
                return
            }

            passwordLiveData.value = Utility.generateRandomString(
                progressLiveData.value!!.toInt(),
                isUpperCaseRequired,
                isLowerCaseRequired,
                isSpecialCharRequired,
                isDigitRequired
            ).toString()
        } else
            statusMessage.value = Event("Please check anyone")
    }
}
