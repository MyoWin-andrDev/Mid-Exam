package com.talentProgramming.midExam.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.ContextCompat
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
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

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("InflateParams")
fun Context.showEditDialog(context : Context , username : String , oldStatus : String){
    val userDb = UserDB(context)
    val binding = DialogEditBinding.inflate(LayoutInflater.from(context))
    val builder = AlertDialog.Builder(context)
    val dialog = builder
        .setView(binding.root)
        .setCancelable(true)
        .create()
    dialog.show()
    binding.apply {
        etStatus.setText(oldStatus)
        //Update Btn
        btUpdate.setOnClickListener {
            if(userDb.updateStatus(etStatus.text.toString(), userDb.getUserId(username))){
                context.showToast(etStatus.text.toString())
                dialog.dismiss()
            }
        }
        //Cancel Btn
        btCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}



