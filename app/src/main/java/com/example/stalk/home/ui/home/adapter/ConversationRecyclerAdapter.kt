package com.example.stalk.home.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.common.model.Conversation
import com.example.stalk.databinding.ItemConversationBinding
import com.example.stalk.home.ui.home.HomeFragment

class ConversationRecyclerAdapter(val fragment: HomeFragment): RecyclerView.Adapter<ConversationRecyclerAdapter.ViewHolder>() {

    private var listConv: List<Conversation> = ArrayList()
    private lateinit var mListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConversationRecyclerAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemConversationBinding = ItemConversationBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: ConversationRecyclerAdapter.ViewHolder, position: Int) {
        var location = 0
        if (listConv[position].group == false){
            location = 1 - listConv[position].member.indexOf(MyFirebase().auth.currentUser?.uid)
        }
        holder.itemBinding.itemConversationTitle.text = listConv[position].title[location]
        holder.itemBinding.itemConversationDetail.text = listConv[position].lastMsg
//        if (listConv[position].read.indexOf(MyFirebase().auth.currentUser?.uid) == -1){
//            holder.itemBinding.itemConversationDetail.setTextColor(R.color.white)
//        } else {
//            holder.itemBinding.itemConversationDetail.setTextColor(R.color.gray)
//        }
        Glide.with(fragment)
            .load(listConv[position].avatar[location])
            .into(holder.itemBinding.itemConversationAvatar)
    }

    override fun getItemCount(): Int {
        return listConv.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListConversation(list: List<Conversation>){
        listConv = list.filter { !it.conId.equals("newConversation") }
        notifyDataSetChanged()
    }

    inner class ViewHolder(val itemBinding: ItemConversationBinding, listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root){

        private var binding: ItemConversationBinding? = null
        init {
            this.binding = itemBinding

            itemBinding.root.setOnClickListener {
                var location = 0
                if (listConv[layoutPosition].group == false){
                    location = 1 - listConv[layoutPosition].member.indexOf(MyFirebase().auth.currentUser?.uid)
                }
                listener.onItemClick(listConv[layoutPosition].conId.toString()
                    , listConv[layoutPosition].title[location].toString()
                    , listConv[layoutPosition].avatar[location].toString())
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(conId: String, title: String, avatar: String)
    }
}