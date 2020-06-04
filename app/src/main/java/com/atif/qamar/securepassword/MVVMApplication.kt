package com.atif.qamar.securepassword

import android.app.Application
import com.atif.qamar.securepassword.db.AppDatabase
import com.atif.qamar.securepassword.factory.*
import com.atif.qamar.securepassword.preferences.PreferenceProvider
import com.atif.qamar.securepassword.repositories.AppRepository

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

/**
 * Created by Atif Qamar on 28-05-2020.
 */
class MVVMApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApplication))
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { AppRepository(instance(), instance()) }
        bind() from provider { PasswordManagerFactory(instance()) }
        bind() from provider { PasswordManagerAddFactory(instance()) }
        bind() from provider { PasswordManagerEditFactory(instance()) }
        bind() from provider { LockScreenPasswordFactory(instance()) }
        bind() from provider { LoginPasswordFactory(instance()) }
        bind() from provider { PasswordChangeFactory(instance()) }
    }
}