package com.talentProgramming.midExam.utilities

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.shashank.sony.fancytoastlib.FancyToast
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.DialogEditAccountBinding

//Toast Function
fun Context.showToast(
    message: String,
    @FancyToast.LayoutType toastType: Int = FancyToast.SUCCESS,
    duration: Int = FancyToast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    FancyToast.makeText(this, message, duration, toastType, withIcon).show()
}

fun Context.showAlertDialog(
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    toastMessage: String? = null,
    onPositiveClick: (() -> Unit)? = null
) {
    val dialog = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(true)
        .setPositiveButton(positiveButtonText) { _, _ ->
            onPositiveClick?.invoke()
        }
        .setNegativeButton(negativeButtonText, null)
        .create()

    dialog.setOnShowListener {
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
            ContextCompat.getColor(this, R.color.md_theme_error)
        )
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
            ContextCompat.getColor(this, R.color.md_theme_tertiary)
        )
    }

    dialog.show()
    toastMessage?.let { showToast(it) }
}

fun Context.confirmPassword(username: String, onSaveClick: () -> Unit) {
    val userDb = UserDB(this)
    val dialogBinding = DialogEditAccountBinding.inflate(LayoutInflater.from(this))

    val dialog = AlertDialog.Builder(this)
        .setView(dialogBinding.root)
        .setCancelable(true)
        .create()

    with(dialogBinding) {
        btSave.setOnClickListener {
            val enteredPassword = etPassword.text.toString()
            val correctPassword = userDb.checkPassword(username)

            when {
                enteredPassword.isEmpty() -> {
                    ilPassword.error = "Password must be filled."
                }

                enteredPassword != correctPassword -> {
                    ilPassword.error = "Your password is incorrect."
                }

                else -> {
                    ilPassword.error = null
                    ilPassword.helperText = ""
                    dialog.dismiss()
                    onSaveClick()
                }
            }
        }

        btCancel.setOnClickListener { dialog.dismiss() }
    }

    dialog.show()
}


fun customStringBuilder(errorList: List<String>): StringBuilder =
    StringBuilder(errorList.joinToString("") { "\u2022 $it" + "\n" })


fun TextInputLayout.checkUsername(
    context: Context,
    username: String,
    oldUsername: String? = null
): Boolean {
    val usernamePattern = Regex("^[A-Z](?=.*[0-9])[A-Za-z0-9._@]{5,19}$")
    val userDb = UserDB(context)

    return when {
        username.isBlank() -> {
            error = customStringBuilder(listOf("Username must be filled."))
            false
        }

        !username.matches(usernamePattern) -> {
            error = customStringBuilder(
                listOf(
                    "Username must start with a capital letter.",
                    "Username must contain a number (0 to 9)."
                )
            )
            false
        }

        userDb.checkUsernameExist(username) && username != oldUsername -> {
            error = customStringBuilder(listOf("Username is already taken."))
            false
        }

        username == oldUsername -> {
            error = null
            helperText = "Username Unchanged"
            true
        }

        else -> {
            error = null
            helperText = "Username is available."
            true
        }
    }
}


fun TextInputLayout.checkPassword(password: String): Boolean {
    val passwordPattern = Regex("^[A-Z](?=.*[0-9])(?=.*[!@#\$%^&*._])[A-Za-z0-9!@#\$%^&*._]{5,19}$")

    return when {
        password.isBlank() -> {
            error = customStringBuilder(listOf("Password must be filled."))
            false
        }

        !password.matches(passwordPattern) -> {
            error = customStringBuilder(
                listOf(
                    "Password must start with a capital letter.",
                    "Password must be at least 6 characters.",
                    "Password must contain a number (0–9).",
                    "Password must contain a special character (!@#\$%^&*._)."
                )
            )
            false
        }

        else -> {
            error = null
            helperText = "Strong Password"
            true
        }
    }
}

fun TextInputLayout.checkRePassword(
    password: String,
    rePassword: String
): Boolean {
    val passwordPattern = Regex("^[A-Z](?=.*[0-9])(?=.*[!@#\$%^&*._])[A-Za-z0-9!@#\$%^&*._]{4,18}$")

    return when {
        rePassword.isBlank() -> {
            error = customStringBuilder(listOf("Password must be filled."))
            false
        }

        !rePassword.matches(passwordPattern) -> {
            error = customStringBuilder(
                listOf(
                    "Password must start with a capital letter.",
                    "Password must be at least 6 characters.",
                    "Password must contain a number (0 to 9).",
                    "Password must contain a special character (!@#\$%^&*._)."
                )
            )
            false
        }

        rePassword.length <= 5 -> {
            error = customStringBuilder(listOf("Password must be more than 5 characters."))
            false
        }

        password != rePassword -> {
            error = customStringBuilder(listOf("Password confirmation does not match."))
            false
        }

        else -> {
            error = null
            helperText = "Strong Password"
            true
        }
    }
}




