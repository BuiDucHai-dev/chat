package com.example.stalk.home.ui.contacts.adapter

import android.annotation.SuppressLint
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stalk.R
import com.example.stalk.common.model.User
import com.example.stalk.databinding.ItemContactBinding
import com.example.stalk.home.ui.contacts.ContactFragment

@SuppressLint("NotifyDataSetChanged")
class ContactRecyclerAdapter(private var fragment: ContactFragment): RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder>() {
    private var listContact: List<User> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactRecyclerAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemContactBinding = ItemContactBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind(position)
//        var lastClickTime: Long = 0
//        holder.itemView.setOnClickListener {
//            if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
//                return@setOnClickListener
//            }
//            lastClickTime = SystemClock.elapsedRealtime()
//            fragment.setOnItemClickListener(listContact[position])
//        }
    }

    override fun getItemCount(): Int {
        return listContact.size
    }

    fun updateListContact(list: List<User>){
        listContact = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val itemBinding: ItemContactBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(position: Int) {
            itemBinding.itemContactFullName.text = listContact[position].fullName
            itemBinding.itemLastSeen.text = listContact[position].dtmLastSeen
            if (listContact[position].avatar != null) {
                Glide.with(fragment)
                    .load(listContact[position].avatar)
                    .into(itemBinding.itemAvatar)
            } else{
                itemBinding.itemAvatar.setImageResource(R.drawable.ic_baseline_person_24)
            }

            var lastClickTime: Long = 0
            itemBinding.root.setOnClickListener {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return@setOnClickListener
                }
                lastClickTime = SystemClock.elapsedRealtime()
                fragment.setOnItemClickListener(listContact[position])
            }
        }

        private var binding: ItemContactBinding? = null
        init {
            this.binding = itemBinding
        }
    }
}