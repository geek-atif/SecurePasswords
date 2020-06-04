package com.atif.qamar.securepassword.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.atif.qamar.securepassword.R
import com.atif.qamar.securepassword.databinding.PasswordChangeFragmentBinding
import com.atif.qamar.securepassword.extensions.toast
import com.atif.qamar.securepassword.factory.PasswordChangeFactory
import com.atif.qamar.securepassword.viewmodel.PasswordChangeViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class PasswordChangeFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()
    private val factory: PasswordChangeFactory by instance<PasswordChangeFactory>()
    private lateinit var viewModel: PasswordChangeViewModel
    private lateinit var binding: PasswordChangeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.password_change_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(PasswordChangeViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                context?.toast(it)
            }
        })
        viewModel.rowId.observe(viewLifecycleOwner, Observer {
            if (it > 0) {
                context?.toast("Data is  updated")
                view?.let {
                    Navigation.findNavController(it).navigate(R.id.action_pwdChange_to_pwdHome)
                };
            }
        })
    }
}
