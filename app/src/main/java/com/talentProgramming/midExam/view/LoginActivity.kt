package com.talentProgramming.midExam.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.shashank.sony.fancytoastlib.FancyToast
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityLoginBinding
import com.talentProgramming.midExam.utilities.INTENT_USERID
import com.talentProgramming.midExam.utilities.INTENT_USERNAME
import com.talentProgramming.midExam.utilities.KEY_IS_USER_LOGGED_IN
import com.talentProgramming.midExam.utilities.KEY_USER_ID
import com.talentProgramming.midExam.utilities.SHARED_PREF_NAME
import com.talentProgramming.midExam.utilities.showToast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userDB: UserDB
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()
        setupClickListeners()
    }

    private fun initializeComponents() {
        userDB = UserDB(this)
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
    }

    private fun setupClickListeners() = with(binding){
            btLogin.setOnClickListener { handleLogin() }
            btRegister.setOnClickListener { navigateToRegister() }
    }

    private fun handleLogin() = with(binding) {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        when  {
            username.isEmpty() -> {
                showToast("Please enter username", toastType = FancyToast.ERROR)
                etUsername.requestFocus()
            }
            password.isEmpty() -> {
                showToast("Please enter password", toastType = FancyToast.ERROR)
                etPassword.requestFocus()
            }
            !userDB.checkUsernameExist(username) -> {
                showToast("Username not found", toastType = FancyToast.ERROR)
            }
            !userDB.verifyUserCredentials(username, password) -> {
                showToast("Invalid password", toastType = FancyToast.ERROR)
            }
            else -> {
                loginSuccess(username, password)
            }
        }
    }

    private fun loginSuccess(username: String, password : String) {
        showToast("Login Successful")

        val userId = userDB.getUserId(username, password)
        // Update shared preferences
        sharedPreferences.edit{
            putBoolean(KEY_IS_USER_LOGGED_IN, true)
            putInt(KEY_USER_ID, userId)
            apply()
        }

        // Navigate to HomeActivity
        Intent(this@LoginActivity, HomeActivity::class.java).apply {
            putExtra(INTENT_USERNAME, username)
            putExtra(INTENT_USERID, userId)
            startActivity(this)
            finish()
        }
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}