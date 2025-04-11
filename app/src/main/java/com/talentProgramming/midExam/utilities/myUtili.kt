package com.talentProgramming.midExam.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.DialogEditAccountBinding
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

@RequiresApi(Build.VERSION_CODES.P)
fun Context.confirmPassword(onSaveClick : (()->Unit)){
    val userDb = UserDB(this)
    val builder = AlertDialog.Builder(this)
    val binding = DialogEditAccountBinding.inflate(LayoutInflater.from(this))
    binding.apply {
        val dialog = builder
            .setCancelable(true)
            .setView(root)
            .create()
        dialog.show()
        btSave.setOnClickListener{
            onSaveClick()
            dialog.dismiss()
        }
        btCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}



