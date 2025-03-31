package com.talentProgramming.midExam.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.talentProgramming.midExam.databinding.ListViewStatusBinding
import com.talentProgramming.midExam.model.StatusModel

class StatusAdapter(val statusList : List<StatusModel>) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    class StatusViewHolder(val binding : ListViewStatusBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder  =
        StatusViewHolder(ListViewStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = statusList.size

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.binding.apply {
            tvUsername.text = statusList[position].username
            tvStatus.text = statusList[position].status
        }
    }
}