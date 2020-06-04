package com.atif.qamar.securepassword.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.atif.qamar.securepassword.util.Constant

/**
 * Created by Atif Qamar on 01-06-2020.
 */

@Entity(tableName = Constant.PASSWORD_LOCK_SCREEN_TABLE_NAME)
data class LockScreenPassword(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var password: String? = null
)