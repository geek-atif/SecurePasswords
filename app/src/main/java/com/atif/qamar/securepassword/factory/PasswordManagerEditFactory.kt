package com.atif.qamar.securepassword.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.atif.qamar.securepassword.repositories.AppRepository
import com.atif.qamar.securepassword.viewmodel.PasswordManagerEditViewModel

/**
 * Created by Atif Qamar on 28-05-2020.
 */
@Suppress("UNCHECKED_CAST")
class PasswordManagerEditFactory(private val appRepository: AppRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PasswordManagerEditViewModel(appRepository) as T
    }

}