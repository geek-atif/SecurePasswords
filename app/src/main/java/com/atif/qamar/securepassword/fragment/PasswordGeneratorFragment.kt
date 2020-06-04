package com.atif.qamar.securepassword.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.atif.qamar.securepassword.R
import com.atif.qamar.securepassword.databinding.PasswordGeneratorFragmentBinding
import com.atif.qamar.securepassword.extensions.toast
import com.atif.qamar.securepassword.util.Utility
import com.atif.qamar.securepassword.viewmodel.PasswordGeneratorViewModel

@SuppressLint("LongLogTag")
class PasswordGeneratorFragment : Fragment() {

    private lateinit var viewModel: PasswordGeneratorViewModel
    lateinit var binding : PasswordGeneratorFragmentBinding
    val TAG ="com.atif.qamar.securepassword.fragment.PasswordGeneratorFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.password_generator_fragment,
            container,
            false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PasswordGeneratorViewModel::class.java)
        binding.viewmodel = viewModel

        binding.imgCopy.setOnClickListener{
           if( binding.tvPassword.text.isEmpty() ){
               context?.toast("Please Generate Password First")
               return@setOnClickListener
           }

           context?.let { it1 -> Utility.copy(it1, binding.tvPassword.text.toString()) }
           context?.toast("Password Copied : ${binding.tvPassword.text}")
        }

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                context?.toast(it)
            }
        })
    }
}
