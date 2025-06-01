package com.talentProgramming.midExam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.talentProgramming.midExam.databinding.ListViewStatusBinding
import com.talentProgramming.midExam.model.StatusModel

class StatusAdapter(
    private var statusList: List<StatusModel>,
    private val moreOnClick: (View, StatusModel) -> Unit
) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    inner class StatusViewHolder(private val binding: ListViewStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StatusModel) = with(binding) {
            tvUsername.text = item.username
            tvStatus.text = item.status
            ivMore.setOnClickListener { moreOnClick(it, item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding =
            ListViewStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bind(statusList[position])
    }

    override fun getItemCount(): Int = statusList.size

    fun updateStatusList(newList: List<StatusModel>) {
        statusList = newList
        notifyDataSetChanged()
    }
}
