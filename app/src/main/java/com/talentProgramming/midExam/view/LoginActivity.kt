package com.talentProgramming.midExam.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.shashank.sony.fancytoastlib.FancyToast
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityLoginBinding
import com.talentProgramming.midExam.utilities.showToast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userDB: UserDB
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDB = UserDB(this)
        sharedPreferences = getSharedPreferences("MY_PREF", MODE_PRIVATE)

        setupListeners()
    }

    private fun setupListeners() = with(binding) {
        btLogin.setOnClickListener {
            handleLogin()
        }

        btRegister.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun handleLogin() = with(binding) {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (!isLoginInputValid(username, password)) return

        if (userDB.checkUsernameExist(username)) {
            val user = userDB.getUser(username, password)
            if (user != null) {
                saveUserSession(user.id, user.userName)
                navigateToHome(username)
                showToast("Login Successfully")
            } else {
                showToast("Incorrect Password", FancyToast.ERROR)
            }
        } else {
            showToast("UserName doesn't exist", toastType = FancyToast.ERROR)
        }
    }

    private fun isLoginInputValid(username: String, password: String): Boolean {
        return when {
            username.isEmpty() -> {
                showToast("Please enter your username", toastType = FancyToast.WARNING)
                false
            }

            password.isEmpty() -> {
                showToast("Please enter your password", toastType = FancyToast.WARNING)
                false
            }

            else -> true
        }
    }


    private fun saveUserSession(userId: Int, username: String) {
        sharedPreferences.edit {
            putBoolean("isUserLoggedIn", true)
            putInt("userId", userId)
            putString("usernameLoggedIn", username)
            apply()
        }
    }

    private fun navigateToHome(username: String) {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("username", username)
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}