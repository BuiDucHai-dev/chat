package com.example.stalk.conversation.adapter

import android.annotation.SuppressLint
import android.view.*
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.stalk.R
import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.conversation.ConversationActivity
import com.example.stalk.conversation.MessViewModel
import com.example.stalk.conversation.model.Message
import com.example.stalk.databinding.ItemMessageLeftBinding
import com.example.stalk.databinding.ItemMessageRightBinding

@SuppressLint("NotifyDataSetChanged")
class MessRecyclerAdapter(activity: ConversationActivity, messViewModel: MessViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var actionMode: ActionMode? = null

    private var activity: ConversationActivity? = activity
    private var messViewModel: MessViewModel? = messViewModel
    private val viewTypeRight: Int = 0
    private val viewTypeLeft: Int = 1
    private var listMess: ArrayList<Message> = ArrayList()

    private var isDeleteMode = false
    private var isSelectAll = false
    private var selectedList = ArrayList<Message>()

    init {
        selectedList.clear()
        messViewModel.getListMessage()
        messViewModel.listMess.observe(activity) {
            listMess = it as ArrayList<Message>
            if (!isDeleteMode) {
                notifyDataSetChanged()
            }
        }
    }

    inner class MessRightViewHolder(mBinding: ItemMessageRightBinding): RecyclerView.ViewHolder(mBinding.root){
        private var binding: ItemMessageRightBinding? = null
        init {
            this.binding = mBinding
        }
        fun bind(position: Int){
            binding?.apply {
                tvContent.text = listMess[position].content
                if (isDeleteMode){
                    cbCheck.visibility = View.VISIBLE
                    cbCheck.isChecked = selectedList.contains(listMess[position])
                } else {
                    cbCheck.visibility = View.GONE
                }
            }
        }
    }

    inner class MessLeftViewHolder(mBinding: ItemMessageLeftBinding): RecyclerView.ViewHolder(mBinding.root){
        private var binding: ItemMessageLeftBinding? = null
        init {
            this.binding = mBinding
        }

        fun bind(position: Int){

            binding?.apply {
                tvContent.text = listMess[position].content
                if (isDeleteMode){
                    cbCheck.visibility = View.VISIBLE
                    cbCheck.isChecked = selectedList.contains(listMess[position])
                } else {
                   cbCheck.visibility = View.GONE
                }
                if(position < listMess.size - 1 && listMess[position].owner == listMess[position + 1].owner){
                    cardImage.visibility = View.GONE
                }else {
                    cardImage.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == viewTypeRight){
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: ItemMessageRightBinding = ItemMessageRightBinding.inflate(layoutInflater, parent, false)
            MessRightViewHolder(binding)
        }else {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: ItemMessageLeftBinding = ItemMessageLeftBinding.inflate(layoutInflater, parent, false)
            MessLeftViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindData(holder)

        holder.itemView.setOnClickListener {
            if (isDeleteMode){
                setClickItem(holder)
            }
        }

        holder.itemView.setOnLongClickListener {
            if (!isDeleteMode){
                (it.context as AppCompatActivity).startActionMode(object :ActionMode.Callback{
                    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                        actionMode = mode
                        mode?.menuInflater?.inflate(R.menu.menu_delete_message, menu)
                        return true
                    }

                    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                        isDeleteMode = true
                        activity?.setActionMode(isDeleteMode)
                        setClickItem(holder)
                        return true
                    }

                    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                        when (item?.itemId){
                            R.id.action_delete -> {
                                messViewModel?.deleteMessage(selectedList, isSelectAll)
                                actionModeDestroy()
                            }
                            R.id.action_select_all -> {
                                selectedList.clear()
                                isSelectAll = if (!isSelectAll){
                                    selectedList.addAll(listMess)
                                    actionMode?.title = activity?.getString(R.string.action_select_all)
                                    true
                                }else {
                                    actionMode?.title = selectedList.size.toString() + " " + activity?.getString(R.string.selected)
                                    false
                                }
                                notifyDataSetChanged()
                            }
                        }
                        return true
                    }

                    override fun onDestroyActionMode(mode: ActionMode?) {
                        actionModeDestroy()
                    }
                })
            } else {
                setClickItem(holder)
            }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return listMess.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (listMess[position].owner != MyFirebase().auth.currentUser?.uid){
            viewTypeLeft
        }else viewTypeRight
    }

    private fun bindData(holder: RecyclerView.ViewHolder){
        if (holder.itemViewType == viewTypeRight){
            (holder as MessRightViewHolder).bind(holder.layoutPosition)
        } else {
            (holder as MessLeftViewHolder).bind(holder.layoutPosition)
        }
    }

    private fun setClickItem(holder: RecyclerView.ViewHolder){
        val message = listMess[holder.layoutPosition]
        if (selectedList.contains(message)){
            selectedList.remove(message)
            holder.itemView.findViewById<CheckBox>(R.id.cb_check).isChecked = false
        } else {
            selectedList.add(message)
            holder.itemView.findViewById<CheckBox>(R.id.cb_check).isChecked = true
        }
        isSelectAll = selectedList.size == listMess.size
        if (selectedList.size == 0){
            actionModeDestroy()
        }
        if (isSelectAll){
            actionMode?.title = activity?.getString(R.string.action_select_all)
        } else {
            actionMode?.title = selectedList.size.toString() + " " + activity?.getString(R.string.selected)
        }
        notifyDataSetChanged()
    }

    private fun actionModeDestroy() {
        actionMode?.finish()
        isDeleteMode = false
        activity?.setActionMode(isDeleteMode)
        selectedList.clear()
        notifyDataSetChanged()
    }
}


