package com.atif.qamar.securepassword.util

import android.view.View
import com.atif.qamar.securepassword.db.entities.PasswordData

/**
 * Created by Atif Qamar on 31-05-2020.
 */

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(view: View, passwordData: PasswordData)
}