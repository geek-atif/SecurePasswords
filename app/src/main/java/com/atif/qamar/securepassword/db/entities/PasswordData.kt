package com.atif.qamar.securepassword.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.atif.qamar.securepassword.util.Constant

/**
 * Created by Atif Qamar on 24-05-2020.
 */

@Entity(tableName = Constant.PASSWORD_DATA_TABLE_NAME)
data class PasswordData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var type_category: String? = null,
    var title: String? = null,
    var user_name: String? = null,
    var password: String? = null,
    var url: String? = null,
    var created_on: Long? = null,
    var updated_on: Long? = null
)