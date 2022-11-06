package com.example.stalk.conversation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stalk.R
import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.conversation.adapter.MessRecyclerAdapter
import com.example.stalk.databinding.ActivityConversationBinding
import com.example.stalk.home.HomeActivity

class ConversationActivity : AppCompatActivity() {

    companion object{
        const val CONVERSATION_ID = "CONVERSATION_ID"
    }

    private lateinit var binding: ActivityConversationBinding
    private lateinit var messViewModel: MessViewModel

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: MessRecyclerAdapter? = null

    private var conId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        conId = bundle?.getString(CONVERSATION_ID, "").toString()

        messViewModel = ViewModelProvider(this, MessViewModelFactory(conId = conId.toString()))
            .get(MessViewModel::class.java)
        messViewModel.getConversation(conId)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        adapter = MessRecyclerAdapter(this, messViewModel)

        binding.rvListMessage.layoutManager = layoutManager
        binding.rvListMessage.adapter = adapter

        binding.btnSend.setOnClickListener {
            if (!binding.edtMess.text.isNullOrEmpty()){
                messViewModel.sendMess(binding.edtMess.text.toString())
                binding.edtMess.text?.clear()
            }
        }

        messViewModel.currentConversation.observe(this) {
            if (it != null) {
                var location = 0
                if (it.group == false) {
                    location = 1 - it.member.indexOf(MyFirebase().auth.currentUser?.uid)
                }
                binding.tvUserName.text = it.title[location]
                binding.tvLastSeen.text = it.dtmModify
                Glide.with(this)
                    .load(it.avatar[location])
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imAvatar)
            }
        }

        messViewModel.deleteSuccess.observe(this){
            if (it == true){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        //Event when click back button on toolbar
        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_conversation_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_clear_history -> {
                messViewModel.clearHistory(conId)
            }
            R.id.action_delete_chat -> {
                messViewModel.deleteChat(conId)
            }
            R.id.action_change_color -> {
                Log.d("hai.bd", "change color")
            }
        }
        return true
    }

    fun setActionMode(deleteMode: Boolean) {
        binding.edtMess.isEnabled = !deleteMode
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }
}