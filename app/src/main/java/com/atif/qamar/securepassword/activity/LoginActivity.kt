package com.atif.qamar.securepassword.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.atif.qamar.securepassword.R
import com.atif.qamar.securepassword.databinding.ActivityLoginPasswordBinding
import com.atif.qamar.securepassword.extensions.toast
import com.atif.qamar.securepassword.factory.LoginPasswordFactory
import com.atif.qamar.securepassword.viewmodel.LoginViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

@SuppressLint("LongLogTag")
class LoginActivity : AppCompatActivity(), KodeinAware {

    private val TAG = "com.atif.qamar.securepassword.activity.LoginActivity"
    override val kodein: Kodein by kodein()
    private val factory: LoginPasswordFactory by instance<LoginPasswordFactory>()
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        Log.i(TAG, viewModel.isPasswordHashAvailable().toString())
        if (!viewModel.isPasswordHashAvailable())
            openLockScreenActivity()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_password)
        binding.viewmodel = viewModel
        viewModel.isPwdHashValid.observe(this, Observer {

            if (it) {
                openMainActivity()
            } else {
                applicationContext?.toast("Wrong Password")
            }
        })

        viewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                application?.toast(it)
            }
        })
    }

    private fun openLockScreenActivity() {
        Intent(this, LockScreenPasswordActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    private fun openMainActivity() {
        Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }
}
