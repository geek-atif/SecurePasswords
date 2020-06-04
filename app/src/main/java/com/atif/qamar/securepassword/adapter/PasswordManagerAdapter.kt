package com.atif.qamar.securepassword.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.atif.qamar.securepassword.R
import com.atif.qamar.securepassword.databinding.RowPasswordLayoutBinding
import com.atif.qamar.securepassword.db.entities.PasswordData
import com.atif.qamar.securepassword.util.RecyclerViewClickListener


/**
 * Created by Atif Qamar on 28-05-2020.
 */
@SuppressLint("LongLogTag")
class PasswordManagerAdapter(
    private val passwordDatas: MutableList<PasswordData>, private val listener:
    RecyclerViewClickListener
) : RecyclerView.Adapter<PasswordManagerAdapter.MyViewHolder>() {

    val TAG = "com.atif.qamar.securepassword.adapter.PasswordManagerAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_password_layout,
                parent,
                false
            )
        )


    override fun getItemCount() = passwordDatas.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.pwdType.text = passwordDatas.get(position).type_category?.take(1)
        holder.binding.pwdTitle.text = passwordDatas.get(position).title
        holder.binding.pwdUsername.text = passwordDatas.get(position).user_name
        holder.binding.layoutConstraint.setOnClickListener {
            listener.onRecyclerViewItemClick(
                holder.binding.layoutConstraint,
                passwordDatas[position]
            )
        }

        holder.binding.imEdit.setOnClickListener {
            listener.onRecyclerViewItemClick(holder.binding.imEdit, passwordDatas[position])
        }
    }


    inner class MyViewHolder(val binding: RowPasswordLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var viewBackground: ConstraintLayout? = binding.viewBackground
        var viewForeground: ConstraintLayout? = binding.viewForeground
    }

    fun removeItem(position: Int) {
        Log.i(TAG, "position : ${position}")
        if (position < 0)
            return

        passwordDatas.removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position)
    }
}


