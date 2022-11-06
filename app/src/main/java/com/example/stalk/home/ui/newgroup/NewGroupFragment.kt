package com.example.stalk.home.ui.newgroup

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.stalk.R
import com.example.stalk.common.Util
import com.example.stalk.common.model.User
import com.example.stalk.databinding.FragmentNewGroupBinding
import com.example.stalk.home.ui.newgroup.adapter.ListPickUserNewGroupAdapter
import com.example.stalk.home.ui.newgroup.adapter.ListUserNewGroupAdapter

class NewGroupFragment : Fragment() {

    private lateinit var viewModel: NewGroupViewModel
    private var _binding: FragmentNewGroupBinding? = null

    private val binding get() = _binding!!

    private var listUserAdapter: RecyclerView.Adapter<ListUserNewGroupAdapter.ViewHolder>? = null
    private var listPickAdapter: RecyclerView.Adapter<ListPickUserNewGroupAdapter.ViewHolder>? = null

    var listPick: List<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, NewGroupModelFactory())
            .get(NewGroupViewModel::class.java)
        listUserAdapter = ListUserNewGroupAdapter(this)
        listPickAdapter = ListPickUserNewGroupAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentNewGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvListContact.adapter = listUserAdapter
        binding.rvPickUserList.adapter = listPickAdapter

        getUserList()
        viewModel.listUser.observe(requireActivity()) {
            val list = it.minus(listPick)
            (listUserAdapter as ListUserNewGroupAdapter).updateListUser(list)
            if ((listUserAdapter as ListUserNewGroupAdapter).itemCount == 0
                && listPickAdapter!!.itemCount == 0){
                binding.tvNoFriend.visibility = View.VISIBLE
            } else {
                binding.tvNoFriend.visibility = View.GONE
            }
        }

        viewModel.listPick.observe(requireActivity()) {
            (listPickAdapter as ListPickUserNewGroupAdapter).updateListPick(it)
            listPick = it
            if (it.isEmpty()) {
                binding.btnNext.visibility = View.GONE
            } else {
                binding.btnNext.visibility = View.VISIBLE
            }
        }

        binding.btnNext.setOnClickListener {
            if(listPick.isNotEmpty()){
                val bundle = Bundle()
                bundle.putInt("numMember", listPick.size)
                for ((count, m) in listPick.withIndex()){
                    val s: String = Util.getGsonParse().toJson(m)
                    bundle.putString(count.toString(), s)
                }
                findNavController().navigate(R.id.action_NewGroup_to_CreateNewGroup, bundle)
            }
        }
    }

    private fun getUserList() {
        viewModel.getUserList()
    }

    fun addToPickList(user: User) {
        viewModel.addToPickList(user)
    }

    fun removeFromPickList(user: User) {
        viewModel.removeFromPickList(user)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }
}