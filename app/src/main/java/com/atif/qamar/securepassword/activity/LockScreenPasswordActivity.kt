package com.atif.qamar.securepassword.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.atif.qamar.securepassword.R
import com.atif.qamar.securepassword.databinding.ActivityLockScreenPasswordBinding
import com.atif.qamar.securepassword.extensions.toast
import com.atif.qamar.securepassword.factory.LockScreenPasswordFactory
import com.atif.qamar.securepassword.viewmodel.LockScreenPasswordViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LockScreenPasswordActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by kodein()
    private val factory: LockScreenPasswordFactory by instance<LockScreenPasswordFactory>()
    private lateinit var viewModel: LockScreenPasswordViewModel
    private lateinit var binding: ActivityLockScreenPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lock_screen_password)
        viewModel = ViewModelProvider(this, factory).get(LockScreenPasswordViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.rowId.observe(this, Observer {
            if (it > -1) {
                openMainActivity()
            } else if (it == 0L) {
                applicationContext?.toast("Data not inserted")
            }
        })

        viewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                application?.toast(it)
            }
        })


    }

    private fun openMainActivity() {
        Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }
}
