package com.talentProgramming.midExam.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.databinding.DialogLayoutBinding
import com.talentProgramming.midExam.databinding.ListViewStatusBinding
import com.talentProgramming.midExam.model.StatusModel
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
                            R.id.ic_edit -> showEditDialog(context)
                            R.id.ic_delete -> context.showToast("Delete")
                        }
                        true
                    }
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun showEditDialog(context : Context){
        val binding = DialogLayoutBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder
            .setView(binding.root)
            .show()
        binding.apply {

        }
    }
}