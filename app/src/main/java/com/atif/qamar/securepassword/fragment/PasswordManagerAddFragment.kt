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
import com.atif.qamar.securepassword.databinding.PasswordManagerAddDataFragmentBinding
import com.atif.qamar.securepassword.extensions.toast
import com.atif.qamar.securepassword.factory.PasswordManagerAddFactory
import com.atif.qamar.securepassword.util.Utility
import com.atif.qamar.securepassword.viewmodel.PasswordManagerAddViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*


@SuppressLint("LongLogTag")
class PasswordManagerAddFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()
    private val factory: PasswordManagerAddFactory by instance<PasswordManagerAddFactory>()
    private lateinit var viewModel: PasswordManagerAddViewModel
    private lateinit var binding: PasswordManagerAddDataFragmentBinding
    val TAG = "com.atif.qamar.securepassword.fragment.PasswordManagerAddFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.password_manager_add_data_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(PasswordManagerAddViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.fetchDropDownPasswordTypeItems().observe(viewLifecycleOwner, Observer { it ->
            val dropDownAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, it)
            binding.filledPwdType.setAdapter(dropDownAdapter)
        })

        viewModel.fetchDropDownPasswordTitleItems().observe(viewLifecycleOwner, Observer { it ->
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
            if (it > -1) {
                openPasswordManagerFragment()

            } else if (it == 0L) {
                context?.toast("Data not inserted")
            }

        })
    }

    private fun openPasswordManagerFragment() {
        context?.let { view?.let { it1 -> Utility.hideKeyBoard(it, it1) } }
        view?.let { Navigation.findNavController(it).navigate(R.id.action_pwdAdd_to_pwdHome) };
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.submit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.submit -> viewModel.savePasswordDetail()
        }
        return super.onOptionsItemSelected(item)
    }


}
