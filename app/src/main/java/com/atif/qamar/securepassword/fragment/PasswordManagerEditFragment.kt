package com.atif.qamar.securepassword.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.atif.qamar.securepassword.R
import com.atif.qamar.securepassword.databinding.PasswordManagerEditDataFragmentBinding
import com.atif.qamar.securepassword.extensions.toast
import com.atif.qamar.securepassword.factory.PasswordManagerEditFactory
import com.atif.qamar.securepassword.util.Constant
import com.atif.qamar.securepassword.util.Utility
import com.atif.qamar.securepassword.viewmodel.PasswordManagerEditViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*

@SuppressLint("LongLogTag")
class PasswordManagerEditFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()
    private val factory: PasswordManagerEditFactory by instance<PasswordManagerEditFactory>()
    private lateinit var viewModel: PasswordManagerEditViewModel
    private lateinit var binding: PasswordManagerEditDataFragmentBinding
    val TAG = "com.atif.qamar.securepassword.fragment.PasswordManagerEditFragment"
    var passwordId: Int = 0
    var typeCategory: String = ""
    var title: String = ""
    var userName: String = ""
    var password: String = ""
    var url: String = ""
    var createdOn: Long = 0L
    var updatedOn: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.password_manager_edit_data_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(PasswordManagerEditViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.fetchDropDownPasswordTypeItems().observe(viewLifecycleOwner, Observer { it ->
            val dropDownAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, it)
            binding.filledPwdType.setAdapter(dropDownAdapter)
        })

        viewModel.fetchDropDownPasswordTitleItems().observe(viewLifecycleOwner, Observer { it ->
            Log.i(TAG, "fetchDropDownPasswordTitleItems ${it}")
            val dropDownAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, it)
            binding.filledPwdTitle.setAdapter(dropDownAdapter)
        })

        binding.filledPwdTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val name: String = p0.toString().toLowerCase(Locale.ROOT)
                if (name.isNullOrEmpty())
                    return
                val resID = resources.getIdentifier(name, "string", context?.packageName)
                Log.i(TAG, "filledPwdTitle : ${getString(resID)}")
                binding.textFieldUrl.setText(resID)
                binding.textFieldUrl.isEnabled = false
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                context?.toast(it)
            }
        })

        viewModel.rowId.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "rowId  : ${it}")
            if (it > -1) {
                openPasswordManagerFragment()
            } else if (it == 0L) {
                context?.toast("Data not inserted")
            }
        })

        displayValue(arguments)
    }

    private fun displayValue(arguments: Bundle?) {
        arguments?.let {
            passwordId = PasswordManagerEditFragmentArgs.fromBundle(it).passwordId
            typeCategory = PasswordManagerEditFragmentArgs.fromBundle(it).typeCategory
            title = PasswordManagerEditFragmentArgs.fromBundle(it).title
            userName = PasswordManagerEditFragmentArgs.fromBundle(it).userName
            password = PasswordManagerEditFragmentArgs.fromBundle(it).password
            url = PasswordManagerEditFragmentArgs.fromBundle(it).url
            createdOn = PasswordManagerEditFragmentArgs.fromBundle(it).createdOn
            updatedOn = PasswordManagerEditFragmentArgs.fromBundle(it).updatedOn

        }!!
        viewModel.typeCategory = typeCategory
        viewModel.id = passwordId
        viewModel.userName = userName
        viewModel.password = Utility.getDecryptData(password)!!.trim()
        viewModel.url = url
        viewModel.createdOn = createdOn
        if (typeCategory.equals(Constant.OTHER) || typeCategory.equals(Constant.PERSONAL))
            viewModel.title_ = title
        else
            viewModel.title = title

    }

    private fun openPasswordManagerFragment() {
        context?.let { view?.let { it1 -> Utility.hideKeyBoard(it, it1) } }
        view?.let { Navigation.findNavController(it).navigate(R.id.action_pwdEdit_to_pwdHome) };
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.submit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.submit -> viewModel.updatePasswordDetail()
        }
        return super.onOptionsItemSelected(item)
    }
}

