package com.example.stalk.home.ui.find_friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.stalk.common.Comm
import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.common.model.User
import com.example.stalk.databinding.FragmentFindFriendBinding

class FindFriendFragment : Fragment() {

    private var _binding: FragmentFindFriendBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FindFriendViewModel

    var phoneNum: String = ""
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, FindFriendModelFactory())
            .get(FindFriendViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFind.setOnClickListener {
            binding.ccp.registerCarrierNumberEditText(binding.etPhone)
            phoneNum = binding.ccp.fullNumberWithPlus.replace(" ", "")
            viewModel.findFriend(phoneNum)
            binding.progressBar.visibility = View.VISIBLE
            Comm().hideKeyBoard(requireActivity())
        }

        viewModel.reUser.observe(requireActivity()){
            binding.progressBar.visibility = View.GONE
            if (it != null){
                user = it
                binding.rlUser.visibility = View.VISIBLE
                binding.tvUserNotExist.visibility = View.GONE
                binding.tvName.text = it.fullName
                binding.tvDtmLastSeen.text = it.dtmLastSeen
                Glide.with(requireActivity())
                    .load(it.avatar)
                    .circleCrop()
                    .into(binding.itemAvatar)

                if (user!!.userId.toString() == MyFirebase().auth.currentUser!!.uid
                    || (user!!.friend != null
                            && user!!.friend!!.contains(MyFirebase().auth.currentUser!!.uid))
                ){
                    binding.btnRequest.visibility = View.GONE
                } else {
                    binding.btnRequest.visibility = View.VISIBLE
                }
            } else {
                binding.rlUser.visibility = View.GONE
                binding.tvUserNotExist.visibility = View.VISIBLE
            }
        }

        binding.btnRequest.setOnClickListener {
            binding.btnRequest.visibility = View.GONE
            requestFriend(user!!)
        }
    }

    private fun requestFriend(user: User) {
        viewModel.requestFriend(user)
    }
}