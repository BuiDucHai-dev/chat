package com.example.stalk.home.ui.newgroup.createnewgroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stalk.common.model.User
import com.example.stalk.databinding.ItemContactBinding

class ListUserCreateGroupAdapter(val fragment: CreateNewGroupFragment, private val listUser: List<User>): RecyclerView.Adapter<ListUserCreateGroupAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListUserCreateGroupAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding.itemContactFullName.text = listUser[position].fullName
        holder.itemBinding.itemLastSeen.text = listUser[position].dtmLastSeen
        Glide.with(fragment)
            .load(listUser[position].avatar.toString())
            .into(holder.itemBinding.itemAvatar)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    inner class ViewHolder(val itemBinding: ItemContactBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private var binding: ItemContactBinding? = null
        init {
            this.binding = itemBinding
        }
    }


}