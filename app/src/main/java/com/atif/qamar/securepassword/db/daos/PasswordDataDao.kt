package com.atif.qamar.securepassword.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.atif.qamar.securepassword.db.entities.PasswordData
import com.atif.qamar.securepassword.util.Constant

/**
 * Created by Atif Qamar on 24-05-2020.
 */
@Dao
interface PasswordDataDao {

    @Insert
    suspend fun addPasswordData(passwordData: PasswordData): Long

    @Update
    suspend fun updatePasswordData(passwordData: PasswordData): Int

    @Delete
    suspend fun deletePasswordData(passwordData: PasswordData): Int

    @Query("SELECT * FROM ${Constant.PASSWORD_DATA_TABLE_NAME} ORDER BY id DESC")
    fun getPasswordDatas(): LiveData<MutableList<PasswordData>>
}