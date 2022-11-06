package com.example.stalk.invite_friend.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stalk.R
import com.example.stalk.databinding.ItemContactOnlyAvatarBinding
import com.example.stalk.invite_friend.InviteFriendActivity
import com.example.stalk.invite_friend.model.PhoneContact

@SuppressLint("NotifyDataSetChanged")
class SelectAdapter(private val activity: InviteFriendActivity): RecyclerView.Adapter<SelectAdapter.ViewHolder>() {

    private var list: ArrayList<PhoneContact> = ArrayList()

    inner class ViewHolder(itemBinding: ItemContactOnlyAvatarBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int) {
            if (list[position].uri != null) {
                Glide.with(activity)
                    .load(list[position].uri)
                    .circleCrop()
                    .into(binding.imAvatar)
            } else {
                binding.imAvatar.setImageResource(R.drawable.ic_baseline_person_24)
            }
            binding.tvName.setBackgroundColor(Color.parseColor(list[position].color))
            binding.tvName.text = list[position].name[0].toString()
        }

        private var binding = itemBinding

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemContactOnlyAvatarBinding = ItemContactOnlyAvatarBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectAdapter.ViewHolder, position: Int) {
        holder.bind(position)
        holder.itemView.setOnClickListener {
            val c = list[position]
            list.remove(c)
            activity.onSelectAdapterListener(c)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun onReceiveContact(contact: PhoneContact) {
        if (list.contains(contact)){
            list.remove(contact)
        } else {
            list.add(contact)
        }
        notifyDataSetChanged()
    }

    fun getSelectedList() : ArrayList<PhoneContact>{
        return list
    }

    fun updateSelectedList(reList: ArrayList<PhoneContact>) {
        list = reList
        notifyDataSetChanged()
    }
}