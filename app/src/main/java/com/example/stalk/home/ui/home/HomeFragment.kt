package com.example.stalk.home.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stalk.R
import com.example.stalk.conversation.ConversationActivity
import com.example.stalk.databinding.FragmentHomeBinding
import com.example.stalk.home.ui.home.adapter.ConversationRecyclerAdapter

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ConversationRecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProvider(this, HomeViewModelFactory())
            .get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(requireContext())
        adapter = ConversationRecyclerAdapter(this)

        binding.rvListConversation.layoutManager = layoutManager
        binding.rvListConversation.adapter = adapter

        getConversationList()

        homeViewModel.list.observe(requireActivity()) {
            (adapter as ConversationRecyclerAdapter).updateListConversation(it)
        }

        (adapter as ConversationRecyclerAdapter).setOnItemClickListener(object : ConversationRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(conId: String, title: String, avatar: String) {
                val intent = Intent(requireActivity(), ConversationActivity::class.java)
                intent.putExtra(ConversationActivity.CONVERSATION_ID, conId)
                startActivity(intent)
                requireActivity().finish()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        getConversationList()
    }

    private fun getConversationList() {
        homeViewModel.getConversationList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        //inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_conv_search -> {
                Toast.makeText(requireContext(), "Home Search", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}