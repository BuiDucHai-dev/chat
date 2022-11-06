package com.example.stalk.home.ui.newgroup.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stalk.common.model.User
import com.example.stalk.databinding.ItemContactOnlyAvatarBinding
import com.example.stalk.home.ui.newgroup.NewGroupFragment

class ListPickUserNewGroupAdapter(val fragment: NewGroupFragment) : RecyclerView.Adapter<ListPickUserNewGroupAdapter.ViewHolder>() {

    private var listUser: List<User> = ArrayList()

    inner class ViewHolder(val itemBinding: ItemContactOnlyAvatarBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private var binding: ItemContactOnlyAvatarBinding? = null
        init {
            this.binding = itemBinding
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListPickUserNewGroupAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemContactOnlyAvatarBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListPickUserNewGroupAdapter.ViewHolder, position: Int) {
        Glide.with(fragment)
            .load(listUser[position].avatar.toString())
            .into(holder.itemBinding.imAvatar)
        holder.itemView.setOnClickListener {
            removeFromPickList(listUser[position])
        }
        holder.itemBinding.tvName.text = listUser[position].fullName?.get(0).toString()
    }

    private fun removeFromPickList(user: User) {
        fragment.removeFromPickList(user)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListPick(list: List<User>?) {
        listUser = list!!
        notifyDataSetChanged()
    }
}