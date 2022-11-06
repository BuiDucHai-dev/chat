package com.example.stalk.home.ui.newgroup.createnewgroup

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.example.stalk.R
import com.example.stalk.common.Util
import com.example.stalk.common.model.User
import com.example.stalk.conversation.ConversationActivity
import com.example.stalk.databinding.FragmentCreateNewGroupBinding
import com.example.stalk.home.HomeActivity

class CreateNewGroupFragment : Fragment() {

    private lateinit var viewModel: CreateNewGroupViewModel
    var list: List<User> = ArrayList()

    private var _binding: FragmentCreateNewGroupBinding? = null
    private val binding get() = _binding!!

    private var adapter: RecyclerView.Adapter<ListUserCreateGroupAdapter.ViewHolder>? = null

    private var avatar: String = Util.getDefaultAvatar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, CreateNewGroupFactory())
            .get(CreateNewGroupViewModel::class.java)
        val numMem: Int? = arguments?.getInt("numMember", 0)
        var count = 0
        while (count < numMem!!){
            val m = arguments?.getString(count.toString())
            count++
            list = list.plusElement(Util.getGsonParse().fromJson(m, User::class.java))
        }

        adapter = ListUserCreateGroupAdapter(this, list)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNewGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvListContact.adapter = adapter

        binding.imAvatar.setOnClickListener {
            setAvatar(binding)
        }

        var lastClickTime: Long = 0;
        binding.btnNext.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                return@setOnClickListener
            }
            lastClickTime = SystemClock.elapsedRealtime()

            if (binding.etGrpName.text.isNullOrEmpty()){
                Toast.makeText(requireContext(), R.string.insert_group_name, Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.createNewConversation(list, (activity as HomeActivity).getUser()
                , avatar
                , binding.etGrpName.text.toString())
            }
        }

        viewModel.conversationId.observe(requireActivity()){
            val intent = Intent(requireActivity(), ConversationActivity::class.java)
            intent.putExtra(ConversationActivity.CONVERSATION_ID, it)
            startActivity(intent)
        }
    }

    private fun setAvatar(binding: FragmentCreateNewGroupBinding) {
        val kwikPicker = KwikPicker.Builder(requireContext(),
            imageProvider = { imageView, uri ->
                Glide.with(requireContext())
                    .load(uri)
                    .into(imageView)
            },
            onImageSelectedListener = {
                Glide.with(requireContext())
                    .load(it)
                    .circleCrop()
                    .into(binding.imAvatar)
                avatar = it.toString()
            },
            peekHeight = 1200)
            .create(requireContext())
        kwikPicker.show(parentFragmentManager)
    }
}