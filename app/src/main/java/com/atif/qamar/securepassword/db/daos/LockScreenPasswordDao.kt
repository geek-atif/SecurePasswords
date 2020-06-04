package com.atif.qamar.securepassword.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.atif.qamar.securepassword.db.entities.LockScreenPassword
import com.atif.qamar.securepassword.util.Constant

/**
 * Created by Atif Qamar on 01-06-2020.
 */
@Dao
interface LockScreenPasswordDao {

    @Insert
    suspend fun addLockScreenPassword(lockScreenPassword: LockScreenPassword): Long

    @Update
    suspend fun updateLockScreenPassword(lockScreenPassword: LockScreenPassword): Int

    @Query("SELECT * FROM ${Constant.PASSWORD_LOCK_SCREEN_TABLE_NAME}")
    suspend fun getLockScreenPassword(): LockScreenPassword
}