package com.atif.qamar.securepassword.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atif.qamar.securepassword.db.entities.PasswordData
import com.atif.qamar.securepassword.repositories.AppRepository
import com.atif.qamar.securepassword.util.Constant
import com.atif.qamar.securepassword.util.Event
import com.atif.qamar.securepassword.util.Utility
import kotlinx.coroutines.launch

@SuppressLint("LongLogTag")
class PasswordManagerAddViewModel(private val appRepository: AppRepository) : ViewModel() {

    val TAG = "com.atif.qamar.securepassword.viewmodel.PasswordManagerAddViewModel"
    var typeCategory: String? = null
    var title: String? = null
    var title_: String? = null
    var userName: String? = null
    var password: String? = null
    var url: String? = null
    var updatedOn = 0L
    var row = 0

    private val _rowId = MutableLiveData<Long>()
    val rowId: LiveData<Long>
        get() = _rowId

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    private val textInputDropdownLiveData = MutableLiveData<Boolean>()
    val textInputDropdown: LiveData<Boolean>
        get() = textInputDropdownLiveData

    private val textInputTitleLiveData = MutableLiveData<Boolean>()
    val textInputTitle: LiveData<Boolean>
        get() = textInputTitleLiveData

    init {
        _rowId.value = -1
        row = 0
        textInputDropdownLiveData.value = true
        textInputTitleLiveData.value = false
    }


    private val mDropdownData = MutableLiveData<List<String>>()
    fun fetchDropDownPasswordTypeItems(): LiveData<List<String>> {
        mDropdownData.value = Constant.LIST_OF_PASSWORD_TYPE_
        return mDropdownData
    }

    private val mDropdownTitleData = MutableLiveData<List<String>>()
    fun fetchDropDownPasswordTitleItems(): LiveData<List<String>> {
        mDropdownTitleData.value = Constant.LIST_OF_SOCIAL_
        return mDropdownTitleData
    }

    fun onTextChanged(ch: CharSequence, start: Int, before: Int, count: Int) {
        when (ch.toString()) {
            Constant.SOCIAL -> {
                mDropdownTitleData.value = Constant.LIST_OF_SOCIAL_
                textInputDropdownLiveData.value = true
                textInputTitleLiveData.value = false
            }
            Constant.SHOPPING -> {
                mDropdownTitleData.value = Constant.LIST_OF_SHOPPING_
                textInputDropdownLiveData.value = true
                textInputTitleLiveData.value = false
            }
            Constant.TRAVEL -> {
                mDropdownTitleData.value = Constant.LIST_OF_TRAVEL_
                textInputDropdownLiveData.value = true
                textInputTitleLiveData.value = false
            }
            Constant.WORK -> {
                mDropdownTitleData.value = Constant.LIST_OF_WORK_
                textInputDropdownLiveData.value = true
                textInputTitleLiveData.value = false
            }
            Constant.PERSONAL -> {
                textInputDropdownLiveData.value = false
                textInputTitleLiveData.value = true
                Log.i(
                    TAG,
                    "PERSONAL  textInputDropdown : ${textInputDropdown.value} textInputTitle ${textInputTitle.value}"
                )
            }
            Constant.OTHER -> {

                textInputDropdownLiveData.value = false
                textInputTitleLiveData.value = true
                Log.i(
                    TAG,
                    "OTHER  textInputDropdown : $textInputDropdown.value textInputTitle ${textInputTitle.value}"
                )
            }
        }
    }

    fun savePasswordDetail() {

        if (typeCategory.isNullOrEmpty()) {
            statusMessage.value = Event("Please select type")
            return
        }

        if (title.isNullOrEmpty() && !typeCategory.equals(Constant.PERSONAL) && !typeCategory.equals(
                Constant.OTHER
            )
        ) {
            statusMessage.value = Event("Please select title for $typeCategory")
            return
        }

        if (title_.isNullOrEmpty() && (typeCategory.equals(Constant.PERSONAL) || typeCategory.equals(
                Constant.OTHER
            ))
        ) {
            statusMessage.value = Event("Please enter title for $typeCategory")
            return
        }

        if (userName.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter username")
            return
        }

        if (password.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter password")
            return
        }

        insert(
            PasswordData(
                row,
                typeCategory?.trim(),
                title?.trim() ?: title_?.trim(),
                userName?.trim(),
                Utility.encrypt
                    (
                    Utility.padString(password?.trim().toString())
                        .toString()
                ),
                url?.trim(),
                Utility.getCurrentTimeStamp(),
                updatedOn
            )
        )
    }

    private fun insert(passwordData: PasswordData) = viewModelScope.launch {
        val newRowId = appRepository.insert(passwordData)
        if (newRowId > -1)
            _rowId.value = 1
        else
            _rowId.value = 0L
    }

}



