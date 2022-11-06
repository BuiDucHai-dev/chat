package com.example.stalk.home.ui.newgroup.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stalk.common.model.User
import com.example.stalk.databinding.ItemContactBinding
import com.example.stalk.home.ui.newgroup.NewGroupFragment

class ListUserNewGroupAdapter(val fragment: NewGroupFragment) : RecyclerView.Adapter<ListUserNewGroupAdapter.ViewHolder>() {

    private var listUser: List<User> = ArrayList()

    inner class ViewHolder(val itemBinding: ItemContactBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private var binding: ItemContactBinding? = null
        init {
            this.binding = itemBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserNewGroupAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListUserNewGroupAdapter.ViewHolder, position: Int) {
        holder.itemBinding.itemContactFullName.text = listUser[position].fullName
        holder.itemBinding.itemLastSeen.text = listUser[position].dtmLastSeen
        Glide.with(fragment)
            .load(listUser[position].avatar.toString())
            .into(holder.itemBinding.itemAvatar)

        holder.itemView.setOnClickListener {
            adToPickList(position)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    private fun adToPickList(position: Int) {
        fragment.addToPickList(listUser[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListUser(listUser: List<User>){
        this.listUser = listUser
        notifyDataSetChanged()
    }
}