package com.talentProgramming.midExam.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.databinding.ListViewStatusBinding
import com.talentProgramming.midExam.model.StatusModel
import com.talentProgramming.midExam.utilities.showAlertDialog
import com.talentProgramming.midExam.utilities.showEditDialog
import com.talentProgramming.midExam.utilities.showToast

class StatusAdapter(val context : Context, private val statusList : List<StatusModel>) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {
    class StatusViewHolder(val binding : ListViewStatusBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder  =
        StatusViewHolder(ListViewStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = statusList.size

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.binding.apply {
            tvUsername.text = statusList[position].username
            tvStatus.text = statusList[position].status
            //Pop-Up Menu
            ivMore.setOnClickListener {
                val popUp = PopupMenu(context, ivMore).apply {
                    menuInflater.inflate(R.menu.menu_item_status, menu)
                    show()
                    setOnMenuItemClickListener{ item ->
                        when(item.itemId){
                            R.id.ic_edit -> context.showEditDialog(context, statusList[position].status)
                            R.id.ic_delete -> context.showAlertDialog("Delete Status?", "Are you sure want to delete this status?", "Delete", "Cancel")
                        }
                        true
                    }
                }
            }
        }
    }

}