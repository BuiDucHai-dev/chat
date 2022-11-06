package com.example.stalk.invite_friend

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.stalk.R
import com.example.stalk.common.Comm
import com.example.stalk.databinding.ActivityInviteFriendBinding
import com.example.stalk.home.HomeActivity
import com.example.stalk.invite_friend.adapter.InviteFriendAdapter
import com.example.stalk.invite_friend.adapter.SelectAdapter
import com.example.stalk.invite_friend.model.PhoneContact

class InviteFriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteFriendBinding

    private lateinit var viewModel: InviteFriendViewModel

    private lateinit var adapter: InviteFriendAdapter
    private lateinit var selectAdapter: SelectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Comm().checkContactPermission(this)){
            val permission = arrayOf(
                Manifest.permission.READ_CONTACTS)
            ActivityCompat.requestPermissions(this, permission, Comm.REQUEST_PERMISSION_CODE)
        }
        
        binding = ActivityInviteFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =  ViewModelProvider(this, InviteFriendViewModelFactory())
            .get(InviteFriendViewModel::class.java)

        viewModel.getContactList(this)

        adapter = InviteFriendAdapter(this)
        selectAdapter = SelectAdapter(this)

        binding.rvListPhoneContact.adapter = adapter
        binding.rvPickUserList.adapter = selectAdapter

        viewModel.listContact.observe(this){
            if (it.isNotEmpty()){
                binding.rvListPhoneContact.visibility = View.VISIBLE
                binding.tvNoPhone.visibility = View.GONE
                adapter.updatePhoneContactList(list = it)
            } else {
                binding.rvListPhoneContact.visibility = View.GONE
                binding.tvNoPhone.visibility = View.VISIBLE
            }
        }

        binding.tvInvite.setOnClickListener {
            if (selectAdapter.itemCount == 0){
                Toast.makeText(this, getString(R.string.you_must_select_contact)
                    , Toast.LENGTH_SHORT).show()
            } else {
                viewModel.moveToSendMessage(this, selectAdapter.getSelectedList())
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        selectAdapter.updateSelectedList(arrayListOf())
        adapter.resetSelectedList()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    fun onInviteFriendAdapterListener(contact: PhoneContact) {
        selectAdapter.onReceiveContact(contact)
        binding.rvPickUserList.scrollToPosition(selectAdapter.itemCount - 1)
    }

    fun onSelectAdapterListener(c: PhoneContact) {
        adapter.onReceiverContact(c)
    }
}