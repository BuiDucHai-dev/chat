package com.example.stalk.conversation.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.stalk.conversation.MessViewModel
import com.example.stalk.conversation.MessViewModelFactory
import com.example.stalk.conversation.adapter.MessRecyclerAdapter
import com.example.stalk.databinding.FragmentConversationBinding

class ConversationFragment(val conId: String) : Fragment() {

    private lateinit var messViewModel: MessViewModel
    private var _binding: FragmentConversationBinding? = null

    private val binding get() = _binding!!

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: MessRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messViewModel = ViewModelProvider(this, MessViewModelFactory(conId = conId.toString()))
            .get(MessViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConversationBinding.inflate(inflater, container, false)
        return binding.root
    }
}