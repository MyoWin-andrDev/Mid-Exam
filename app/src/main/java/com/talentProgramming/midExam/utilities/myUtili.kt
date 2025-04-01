package com.talentProgramming.midExam.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import com.talentProgramming.midExam.R

//Toast Function
fun Context.showToast(value : String){
    Toast.makeText(this, value, Toast.LENGTH_LONG).show()
}
//Dialog Function
@SuppressLint("SuspiciousIndentation")
fun Context.showAlertDialog(
    title : String,
    message : String ,
    positiveButtonText : String,
    negativeButtonText : String,
    onPositiveClick : (() -> Unit)? = null
){
    val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton(positiveButtonText){dialog, _ ->
                onPositiveClick?.invoke()
                dialog.dismiss()
            }
            .setNegativeButton(negativeButtonText){dialog,_ ->
                dialog.dismiss()
            }.show()
}
//Pop-Up Menu Function
fun Context.showPopUpMenu(button: ImageButton){
    val popUp = PopupMenu(this, button).apply {
        menuInflater.inflate(R.menu.menu_item_status, menu)
        show()
        setOnMenuItemClickListener{ item ->
            when(item.itemId){
                R.id.ic_edit -> showToast("Edit")
                R.id.ic_delete -> showToast("Delete")
            }
            true
        }
    }
}