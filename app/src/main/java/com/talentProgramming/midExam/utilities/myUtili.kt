package com.talentProgramming.midExam.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.shashank.sony.fancytoastlib.FancyToast
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityLoginBinding
import com.talentProgramming.midExam.databinding.ActivityRegisterBinding
import com.talentProgramming.midExam.databinding.DialogEditAccountBinding
//Toast Function
fun Context.showToast(value : String){
    FancyToast.makeText(this, value ,FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show()
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
fun Context.confirmPassword(username : String, onSaveClick : (()->Unit)){
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
            if(etPassword.text.toString() == userDb.checkPassword(username)){
                ilPassword.helperText = ""
                onSaveClick()
            }
            else if(etPassword.text.toString().isEmpty()){
                ilPassword.error = "Password must be filled."
            }
            else{
                ilPassword.error = "Your password is incorrect."
            }
        }
        btCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}

fun customStringBuilder(errorList : List<String>) : StringBuilder{
    val stringBuilder = StringBuilder()
    errorList.forEach {
        stringBuilder
            .append("\u2022")//Bullet Point
            .append(" ")
            .append(it)
            .append("\n")
    }
    return stringBuilder
}
//Username Text Input Validation Check
@RequiresApi(Build.VERSION_CODES.P)
fun TextInputLayout.checkUsername(context: Context,username : String, oldUsername : String? = null) : Boolean {
    val USERNAME_VALID_PATTERN = Regex("^[A-Z](?=.*[0-9])[A-Za-z0-9._@]{5,19}\$")
    val userDb = UserDB(context)
    var isUsernameChecked = false
    this.apply {
        if (username.isNotEmpty()) {
            if (username.matches(USERNAME_VALID_PATTERN)) {
                if (!userDb.checkUsernameExist(username)) {
                    helperText = "Username is available."
                    isUsernameChecked = true
                }else if(oldUsername?.isNotEmpty() == true && username == oldUsername){
                    helperText = "Username Unchanged"
                    isUsernameChecked = true
                }
                else {
                    error = customStringBuilder(listOf("Username is already taken."))
                }
            } else {
                error = customStringBuilder(
                    listOf(
                        "Username must be start with capital letter.",
                        "Username must be contain a number(0 to 9)."
                    )
                )
            }
        } else {
            error = customStringBuilder(
                listOf("Username must be filled.")
            )
        }
    }
    return isUsernameChecked
}

@RequiresApi(Build.VERSION_CODES.P)
fun TextInputLayout.checkPassword(context : Context, password : String) : Boolean{
    val PASSWORD_VALID_PATTERN = Regex("^[A-Z](?=.*[0-9])(?=.*[!@#\$%^&*._])[A-Za-z0-9!@#\$%^&*._]{4,18}\$")
    val userDb = UserDB(context)
    var isPasswordChecked = false
    if(password.isNotEmpty()){
        if(password.matches(PASSWORD_VALID_PATTERN)){
            if(password.length > 5){
                helperText = "Strong Password"
                isPasswordChecked = true
            }
        }else{
            error = customStringBuilder(
                listOf(
                    "Password must be start with capital letter.",
                    "Password must be at least 6 characters.",
                    "Password must be contained a number(0 to 9).",
                    "Password must be contained a special character(!@#\$%^&*._).",
                )
            )
        }
    }
    else{
        error = customStringBuilder(
            listOf("Password must be filled.")
        )
    }
    return isPasswordChecked
}

@RequiresApi(Build.VERSION_CODES.P)
fun TextInputLayout.checkRePassword(context: Context, password : String, rePassword : String) : Boolean {
    val PASSWORD_VALID_PATTERN = Regex("^[A-Z](?=.*[0-9])(?=.*[!@#\$%^&*._])[A-Za-z0-9!@#\$%^&*._]{4,18}\$")
    val userDb = UserDB(context)
    var isRePasswordChecked = false
    if(rePassword.isNotEmpty()){
        if(rePassword.matches(PASSWORD_VALID_PATTERN)){
            if(rePassword.length > 5){
                helperText = "Strong Password"
                if(password == rePassword){
                    isRePasswordChecked = true
                }
                else{
                    error = customStringBuilder(listOf("Password confirmation does not match."))
                    error = customStringBuilder(listOf("Password confirmation does not match."))
                }
            }
        }else{
            error = customStringBuilder(
                listOf(
                    "Password must be start with capital letter.",
                    "Password must be at least 6 characters.",
                    "Password must be contained a number(0 to 9).",
                    "Password must be contained a special character(!@#\$%^&*._).",
                )
            )
        }
    }
    else{
        error = customStringBuilder(
            listOf("Password must be filled.")
        )
    }
    return isRePasswordChecked
}


