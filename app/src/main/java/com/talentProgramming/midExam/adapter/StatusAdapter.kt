package com.talentProgramming.midExam.adapter


import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.databinding.ListViewStatusBinding
import com.talentProgramming.midExam.model.StatusModel
import com.talentProgramming.midExam.view.HomeActivity

class StatusAdapter(
    private var statusList: List<StatusModel>,
    private val onActionClick: (statusId: Int, action: HomeActivity.Action) -> Unit
) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    class StatusViewHolder(val binding: ListViewStatusBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder =
        StatusViewHolder(ListViewStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = statusList.size

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.binding.apply {
            tvUsername.text = statusList[position].username
            tvStatus.text = statusList[position].status

            ivMore.setOnClickListener { view ->
                PopupMenu(view.context, view).apply {
                    menuInflater.inflate(R.menu.menu_item_status, menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.ic_edit -> onActionClick(statusList[position].statusId, HomeActivity.Action.EDIT)
                            R.id.ic_delete -> onActionClick(statusList[position].statusId, HomeActivity.Action.DELETE)
                        }
                        true
                    }
                    show()
                }
            }
        }
    }

    fun updateStatusList(newList: List<StatusModel>) {
        statusList = newList
        notifyDataSetChanged()
    }
}