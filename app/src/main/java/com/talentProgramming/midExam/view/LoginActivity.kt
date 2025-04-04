package com.talentProgramming.midExam.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityLoginBinding
import com.talentProgramming.midExam.utilities.showToast

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var userDB : UserDB
    private lateinit var sharedPreferences: SharedPreferences
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        userDB = UserDB(this)
        sharedPreferences = getSharedPreferences("MY_PREF", MODE_PRIVATE)
        binding.apply {
            setContentView(root)
            btLogin.setOnClickListener{
                val etUsername = etUsername.text.toString()
                if(userDB.checkUsernameExist(etUsername)){
                    showToast("Login Successfully")
                    val userId = userDB.getUserId(etUsername)
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.putExtra("username", etUsername)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    //Edit PREF
                    sharedPreferences.edit().apply {
                        putBoolean("isUserLoggedIn", true)
                        putString("usernameLoggedIn" , etUsername)
                        apply()
                    }
                    finish()
                }
                else{
                    showToast("Login Failed!!!")
                }
            }
            btRegister.setOnClickListener{
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }
}