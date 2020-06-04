package com.atif.qamar.securepassword.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.atif.qamar.securepassword.db.daos.LockScreenPasswordDao
import com.atif.qamar.securepassword.db.daos.PasswordDataDao
import com.atif.qamar.securepassword.db.entities.LockScreenPassword
import com.atif.qamar.securepassword.db.entities.PasswordData

/**
 * Created by Atif Qamar on 24-05-2020.
 */

@Database(
    entities = [PasswordData::class, LockScreenPassword::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun getPasswordDatas(): PasswordDataDao
    abstract fun getLockScreenPassword(): LockScreenPasswordDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private var LOCK = Any()
        private val DATABASE_NAME: String = "SecurePassword.db"

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context, DATABASE_NAME).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context, databaseName: String) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                databaseName
            ).build()
    }
}