package com.talentProgramming.midExam.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.databinding.DialogEditBinding
//Toast Function
fun Context.showToast(value : String){
    Toast.makeText(this, value, Toast.LENGTH_LONG).show()
}

//Dialog Function
@SuppressLint("SuspiciousIndentation", "ResourceAsColor")
fun Context.showAlertDialog(
    title : String,
    message : String ,
    positiveButtonText : String,
    negativeButtonText : String,
    toastMessage : String? = null,
    onPositiveClick : (() -> Unit)? = null
){
    val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton(positiveButtonText){ _, _ ->
                onPositiveClick?.invoke()
            }
            .setNegativeButton(negativeButtonText){dialog,_ ->
                dialog.dismiss()
            }.show()
                .apply {
        getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.md_theme_error))
        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.md_theme_tertiary))
    }
    toastMessage?.apply {
        showToast(this)
    }
}

@SuppressLint("InflateParams")
fun Context.showEditDialog(context : Context , oldStatus : String){
    val binding = DialogEditBinding.inflate(LayoutInflater.from(context))
    val builder = AlertDialog.Builder(context)
    builder
        .setView(binding.root)
        .setCancelable(true)
        .show()
        .also { dialog ->
            binding.apply {
                etStatus.setText(oldStatus)
                btUpdate.setOnClickListener {
                    context.showToast(etStatus.text.toString())
                    dialog.dismiss()
                }

                btCancel.setOnClickListener {
                    dialog.dismiss()
                }
            }
        }
}



