package com.talentProgramming.midExam.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shashank.sony.fancytoastlib.FancyToast
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityRegisterBinding
import com.talentProgramming.midExam.utilities.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userDB: UserDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDB = UserDB(this)
        setupListeners()
    }

    private fun setupListeners() = with(binding) {
        btLogin.setOnClickListener {
            navigateToLogin()
        }

        btSignUp.setOnClickListener {
            if (validateInputs()) {
                registerUser()
            } else {
                showToast(
                    "Something Went Wrong. Please Try Again.",
                    toastType = FancyToast.ERROR
                )
            }
        }
    }

    private fun validateInputs(): Boolean = with(binding) {
        val context = this@RegisterActivity
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        val rePassword = etRePassword.text.toString()

        val isUsernameValid = ilUsername.checkUsername(context, username)
        val isPasswordValid = ilPassword.checkPassword(context, password)
        val isRePasswordValid = ilRePassword.checkRePassword(context, password, rePassword)

        return isUsernameValid && isPasswordValid && isRePasswordValid
    }

    private fun registerUser() = with(binding) {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (userDB.insertUser(username, password)) {
            showToast("Your account has been successfully created.")
            navigateToLogin()
            finish()
        } else {
            showToast("Registration failed. Try again.", toastType = FancyToast.ERROR)
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }
}
