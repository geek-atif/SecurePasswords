package com.atif.qamar.securepassword.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.atif.qamar.securepassword.R
import com.atif.qamar.securepassword.activity.MainActivity
import com.atif.qamar.securepassword.adapter.PasswordManagerAdapter
import com.atif.qamar.securepassword.databinding.PasswordMangerFragmentBinding
import com.atif.qamar.securepassword.db.entities.PasswordData
import com.atif.qamar.securepassword.extensions.toast
import com.atif.qamar.securepassword.factory.PasswordManagerFactory
import com.atif.qamar.securepassword.util.RecyclerItemTouchHelper
import com.atif.qamar.securepassword.util.RecyclerViewClickListener
import com.atif.qamar.securepassword.util.Utility
import com.atif.qamar.securepassword.viewmodel.PasswordManagerViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*


@SuppressLint("LongLogTag")
class PasswordManagerFragment : Fragment(), KodeinAware,
    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
    RecyclerViewClickListener {
    override val kodein: Kodein by kodein()
    private lateinit var viewModel: PasswordManagerViewModel
    private lateinit var binding: PasswordMangerFragmentBinding
    private val factory: PasswordManagerFactory by instance()
    private lateinit var adapter: PasswordManagerAdapter
    var passwordDataList = mutableListOf<PasswordData>()

    val TAG = "com.atif.qamar.securepassword.fragment.PasswordManagerFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.password_manger_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(PasswordManagerViewModel::class.java)
        initRecyclerView()
        binding.viewmodel = viewModel
        binding.floatingButtonAdd.setOnClickListener {
            view?.let { Navigation.findNavController(it).navigate(R.id.action_pwdHome_to_pwdAdd) };
        }

        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerView)

    }

    private fun initRecyclerView() {
        viewModel.passwordData.observe(viewLifecycleOwner, Observer { passwordDatas ->
            adapter = PasswordManagerAdapter(passwordDatas, this)
            passwordDataList = passwordDatas
            binding.recyclerView.adapter = adapter
            if (passwordDatas.isEmpty())
                binding.mesgConstraintLayout.visibility = View.VISIBLE
            else
                binding.mesgConstraintLayout.visibility = View.GONE
        })
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is PasswordManagerAdapter.MyViewHolder) {
            // backup of removed item for undo purpose
            viewModel.delete(passwordDataList.get(viewHolder.getAdapterPosition()))
            viewModel.rowId.observe(viewLifecycleOwner, Observer {
                Log.i(TAG, "row ->> ${it}")
                if (it > 0) {
                    adapter.removeItem(viewHolder.getAdapterPosition())
                    if (passwordDataList.isEmpty())
                        binding.mesgConstraintLayout.visibility = View.VISIBLE
                    else
                        binding.mesgConstraintLayout.visibility = View.GONE
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val item: MenuItem = menu.findItem(R.id.action_search)
        val searchView = SearchView((context as MainActivity).supportActionBar!!.themedContext)
        item.setActionView(searchView)
        searchView.queryHint ="Type/Title/UserName"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return serachIteam(newText)
            }
        })
    }

    private fun serachIteam(newText: String): Boolean {
        val passwordDataListNew = mutableListOf<PasswordData>()
        if (newText.isNullOrEmpty() || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }
        for (passwordData in passwordDataList) {
            if (passwordData.type_category?.toLowerCase(Locale.ROOT)!!
                    .contains(newText.toLowerCase(Locale.ROOT)) ||
                passwordData.title?.toLowerCase(Locale.ROOT)!!
                    .contains(newText.toLowerCase(Locale.ROOT))
                || passwordData.user_name?.toLowerCase(Locale.ROOT)!!
                    .contains(newText.toLowerCase(Locale.ROOT))
            ) {
                passwordDataListNew.add(passwordData)
            }
        }

        adapter = PasswordManagerAdapter(passwordDataListNew, this)
        binding.recyclerView.adapter = adapter
        return true
    }


    override fun onRecyclerViewItemClick(view: View, passwordData: PasswordData) {
        when (view.id) {
            R.id.layout_constraint -> {
                context?.let { showDialog(it, passwordData) }
            }

            R.id.im_edit-> {
                val action = PasswordManagerFragmentDirections.actionPwdHomeToPwdEdit(
                    passwordData.id,
                    passwordData.type_category.toString(),
                    passwordData.title.toString(),
                    passwordData.user_name.toString(),
                    passwordData.password.toString(),
                    passwordData.url.toString(),
                    passwordData.created_on!!.toLong(),
                    passwordData.updated_on!!.toLong()
                )
                view.findNavController().navigate(action)
            }
        }
    }


    private fun resetSearch() {
        adapter = PasswordManagerAdapter(passwordDataList, this)
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun showDialog(context: Context, passwordData: PasswordData){
        val dialogBuilder: AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog_password_detail, null)
        dialogBuilder.setView(dialogView)

        val tvType = dialogView.findViewById(R.id.tvType) as TextView
        val tv_title = dialogView.findViewById(R.id.tv_title) as TextView
        val tv_username = dialogView.findViewById(R.id.tv_username) as TextView
        val tv_password = dialogView.findViewById(R.id.tv_password) as TextView
        val tv_url = dialogView.findViewById(R.id.tv_url) as TextView
        val img_copy = dialogView.findViewById(R.id.img_copy) as ImageView

        val decryptPassword  = Utility.getDecryptData(passwordData.password)?.trim()
        tvType.setText(passwordData.type_category)
        tv_title.setText(passwordData.title)
        tv_username.setText(passwordData.user_name)
        tv_password.setText(decryptPassword)
        tv_url.setText(passwordData.url)

        img_copy.setOnClickListener{
            context.let { it1 -> Utility.copy(it1, decryptPassword.toString()) }
            context.toast("Password Copied : ${decryptPassword}")
        }

        val dialog: AlertDialog = dialogBuilder.create()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show()
    }
}
