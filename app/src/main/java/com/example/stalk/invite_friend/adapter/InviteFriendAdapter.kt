package com.example.stalk.invite_friend.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stalk.R
import com.example.stalk.databinding.ItemPhoneContactBinding
import com.example.stalk.invite_friend.InviteFriendActivity
import com.example.stalk.invite_friend.model.PhoneContact

@SuppressLint("NotifyDataSetChanged")
class InviteFriendAdapter(private val activity: InviteFriendActivity): RecyclerView.Adapter<InviteFriendAdapter.ViewHolder>() {

    private var phoneContact: ArrayList<PhoneContact> = arrayListOf()
    private var selectedList: ArrayList<PhoneContact> = arrayListOf()

    var color: List<String> = listOf("#C0C0C0", "#FF0000", "#800000", "#FFFF00", "#808000", "#00FF00", "#0000FF", "#FF00FF")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InviteFriendAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemPhoneContactBinding = ItemPhoneContactBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InviteFriendAdapter.ViewHolder, position: Int) {
        holder.bind(holder.layoutPosition)
        holder.itemView.setOnClickListener {
            val contact = phoneContact[position]
            if (selectedList.contains(contact)){
                selectedList.remove(contact)
            } else {
                selectedList.add(contact)
            }
            activity.onInviteFriendAdapterListener(contact)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return phoneContact.size
    }

    inner class ViewHolder(itemBinding: ItemPhoneContactBinding): RecyclerView.ViewHolder(itemBinding.root){
        private var binding: ItemPhoneContactBinding = itemBinding

        fun bind(position: Int){
            binding.apply {
                phoneContact[position].color = color[position%color.size]
                binding.itemContactFullName.text = phoneContact[position].name
                binding.itemLastSeen.text = phoneContact[position].number

                binding.itemAvatar.setBackgroundColor(Color.parseColor(phoneContact[position].color))

                if (!phoneContact[position].uri.isNullOrEmpty()) {
                    Glide.with(activity)
                        .load(phoneContact[position].uri)
                        .circleCrop()
                        .into(binding.itemAvatar)
                } else {
                    binding.itemAvatar.setImageResource(R.drawable.ic_baseline_person_24)
                }

                if (selectedList.contains(phoneContact[position])){
                    binding.imCheck.visibility = View.VISIBLE
                } else {
                    binding.imCheck.visibility = View.GONE
                }
            }
        }
    }

    fun updatePhoneContactList(list: ArrayList<PhoneContact>){
        phoneContact = list
        notifyDataSetChanged()
    }

    fun onReceiverContact(c: PhoneContact) {
        if(selectedList.contains(c)){
            selectedList.remove(c)
            notifyDataSetChanged()
        }
    }

    fun resetSelectedList(){
        selectedList.clear()
        notifyDataSetChanged()
    }
}