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
fun Context.showToast(
    message : String,
    @FancyToast.LayoutType toastType : Int = FancyToast.SUCCESS,
    duration : Int = FancyToast.LENGTH_SHORT,
    withIcon : Boolean = true
){
    FancyToast.makeText(this, message ,duration ,toastType ,withIcon).show()
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
fun Context.confirmPassword(userId : Int, onSaveClick : (()->Unit)){
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
            val inputQuery = etPassword.text.toString()
            if(inputQuery == userDb.checkPassword(userId)){
                ilPassword.helperText = ""
                onSaveClick()
            }
            else if(inputQuery.isEmpty()){
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

fun customStringBuilder(errorList : List<String>) : StringBuilder =
    StringBuilder(errorList.joinToString(""){"\u2022 $it \n"})



//Username Text Input Validation Check
@RequiresApi(Build.VERSION_CODES.P)
fun TextInputLayout.checkUsername(context: Context,username : String, oldUsername : String? = null) : Boolean {
    val userDb = UserDB(context)
        return when {
            username.isBlank() -> {
                error = customStringBuilder(listOf("Username must be filled."))
                false
            }
            !username.matches(USERNAME_VALID_PATTERN) -> {
                error = customStringBuilder(
                    listOf(
                        "Username must be start with capital letter.",
                        "Username must be contain a number(0 to 9)."
                    )
                )
                false
            }
            oldUsername == username -> {
                helperText = "Username Unchanged"
                true
            }
            !userDb.checkUsernameExist(username) -> {
                helperText = "Username is available."
                true
            }
            else -> {
                error = customStringBuilder(listOf("Username is already taken."))
                false
            }
        }
}

@RequiresApi(Build.VERSION_CODES.P)
fun TextInputLayout.checkPassword(context : Context, password : String) : Boolean{
    val userDb = UserDB(context)
     return when {
        password.isBlank() -> {
            error = customStringBuilder(listOf("Password must be filled."))
            false
        }
        !password.matches(PASSWORD_VALID_PATTERN) -> {
            error = customStringBuilder(
                listOf(
                    "Password must start with capital letter.",
                    "Password must be at least 6 characters.",
                    "Password must contain a number (0 to 9).",
                    "Password must contain a special character (!@#\$%^&*._)."
                )
            )
            false
        }
        else -> {
            helperText = "Strong Password"
            true
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun TextInputLayout.checkRePassword(context: Context, password : String, rePassword : String) : Boolean {
    val userDb = UserDB(context)
    var isRePasswordChecked = false
    return when{
        rePassword.isBlank() -> {
            error = customStringBuilder(listOf("Password must be filled."))
            false
        }
        !rePassword.matches(PASSWORD_VALID_PATTERN) -> {
            error = customStringBuilder(
                listOf(
                    "Password must start with capital letter.",
                    "Password must be at least 6 characters.",
                    "Password must contain a number (0 to 9).",
                    "Password must contain a special character (!@#\$%^&*._)."
                )
            )
            false
        }
        rePassword != password -> {
            error = customStringBuilder(listOf("Password confirmation does not match."))
            false
        }
        else -> {
            helperText = "Strong Password"
            true
        }
    }
}


