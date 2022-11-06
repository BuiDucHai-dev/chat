package com.example.stalk.home.ui.contacts

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stalk.R
import com.example.stalk.common.Comm
import com.example.stalk.common.model.User
import com.example.stalk.conversation.ConversationActivity
import com.example.stalk.databinding.FragmentContactsBinding
import com.example.stalk.home.HomeActivity
import com.example.stalk.home.ui.contacts.adapter.ContactRecyclerAdapter

class ContactFragment : Fragment() {

    private lateinit var contactViewModel: ContactViewModel
    private var _binding: FragmentContactsBinding? = null

    private val binding get() = _binding!!

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: ContactRecyclerAdapter? = null

    private var selectedUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactViewModel = ViewModelProvider(this, ContactViewModelFactory())
            .get(ContactViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(requireContext())
        adapter = ContactRecyclerAdapter(this)

        binding.rcvContactList.layoutManager = layoutManager
        binding.rcvContactList.adapter = adapter

        getContactListByName()

        contactViewModel.listContact.observe(requireActivity()) {
            (adapter as ContactRecyclerAdapter).updateListContact(it)
        }

        contactViewModel.sortType.observe(requireActivity()) {
            if (it == true) {
                getContactListByName()
            } else {
                getContactList()
            }
        }

        contactViewModel.conversationId.observe(requireActivity()) {
            Log.d("hai.bd", "conversationId: " + it)
            if (it.equals(ContactDataSource.CON_ID)) {
                contactViewModel.createNewConversation(
                    selectedUser!!,
                    (activity as HomeActivity).getUser()
                )
            } else {
                val intent = Intent(requireActivity(), ConversationActivity::class.java)
                intent.putExtra(ConversationActivity.CONVERSATION_ID, it)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        binding.tvInviteFriend.setOnClickListener {
            if (!Comm().checkContactPermission(requireActivity())){
                Comm().requestContactPermission(requireActivity())
            } else {
                //startActivity(Intent(requireActivity(), InviteFriendActivity::class.java))
                findNavController().navigate(R.id.action_Contact_to_InviteFriend)
            }
        }

        binding.tvFindFriend.setOnClickListener {
            findNavController().navigate(R.id.action_Contact_to_Find_Friend)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getContactListByName(){
        contactViewModel.getContactListByName()
        adapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getContactList() {
        contactViewModel.getContactList()
        adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_contacts_fragment, menu)

        val searchView: SearchView = menu.findItem(R.id.action_contacts_search).actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        val srcText: TextView = searchView.findViewById(R.id.search_src_text)
        val view: View = searchView.findViewById(R.id.search_close_btn)
        view.setOnClickListener {
            srcText.text = ""
            searchView.onActionViewCollapsed()
        }

        menu.findItem(R.id.action_contacts_search).setOnActionExpandListener(object: MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                Log.d("hai.bd", "onMenuItemActionExpand ")
                menu.findItem(R.id.action_sort_alpha).isEnabled = false
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                Log.d("hai.bd", "onMenuItemActionCollapse ")
                menu.findItem(R.id.action_sort_alpha).isEnabled = true
                return true
            }
        })

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Log.d("hai.bd", "onQueryTextSubmit: $p0")
                updateListContact(p0!!)
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                Log.d("hai.bd", "onQueryTextChange: $p0")
                updateListContact(p0!!)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun updateListContact(prefix: String){
        val list = contactViewModel.contactFilter(prefix)
        adapter!!.updateListContact(list!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_sort_alpha -> {
                Log.d("hai.bd", "change sort")
                contactViewModel.changeSortType()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setOnItemClickListener(user: User) {
        selectedUser = user
        contactViewModel.getConversationId(user.userId!!)
    }
}