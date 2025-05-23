package com.talentProgramming.midExam.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.DialogEditStatusBinding
import com.talentProgramming.midExam.databinding.ListViewStatusBinding
import com.talentProgramming.midExam.model.StatusModel
import com.talentProgramming.midExam.utilities.showAlertDialog
import com.talentProgramming.midExam.utilities.showToast
@SuppressLint("NotifyDataSetChanged")
class StatusAdapter(private val context : Context, private val username : String, private var statusList : List<StatusModel>) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {
    class StatusViewHolder(val binding : ListViewStatusBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder  =
        StatusViewHolder(ListViewStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = statusList.size


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.binding.apply {
            tvUsername.text = statusList[position].username
            tvStatus.text = statusList[position].status
            //Pop-Up Menu
            ivMore.setOnClickListener {
                val userDb = UserDB(context)
                PopupMenu(context, ivMore).apply {
                    menuInflater.inflate(R.menu.menu_item_status, menu)
                    show()
                    setOnMenuItemClickListener{ item ->
                        when(item.itemId){
                            //Btn Edit
                            R.id.ic_edit -> {
                                val dialogBinding = DialogEditStatusBinding.inflate(LayoutInflater.from(context))
                                val builder = AlertDialog.Builder(context)
                                val dialog = builder
                                    .setView(dialogBinding.root)
                                    .setCancelable(true)
                                    .create()
                                dialog.show()
                                dialogBinding.apply {
                                    etStatus.setText(statusList[position].status)
                                    //Update Btn
                                    btUpdate.setOnClickListener {
                                        if(userDb.updateStatus(etStatus.text.toString(), statusList[position].status_id)){
                                            updateStatusList(userDb.getUserUploadStatus(username))
                                            context.showToast("Update Status Successfully")
                                            dialog.dismiss()
                                        }
                                    }
                                    //Cancel Btn
                                    btCancel.setOnClickListener {
                                        dialog.dismiss()
                                    }
                                }

                            }
                            //Btn Delete
                            R.id.ic_delete -> context.showAlertDialog(
                                "Delete Status?",
                                "Are you sure want to delete this status?",
                                "Delete",
                                "Cancel",
                                null,
                                onPositiveClick = {
                                    if(userDb.deleteStatus(statusList[position].status_id)){
                                        updateStatusList(userDb.getUserUploadStatus(username))
                                        context.showToast("Delete Status Successfully")
                                    }
                                }
                            )
                        }
                        true
                    }
                }
            }
        }
    }
    private fun updateStatusList(newList : List<StatusModel>){
        statusList = newList
        notifyDataSetChanged()
    }
}