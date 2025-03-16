package com.talentProgramming.midExam.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityLoginBinding
import com.talentProgramming.midExam.utilities.showToast

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var userDB : UserDB
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        userDB = UserDB(this)
        binding.apply {
            setContentView(root)
            btLogin.setOnClickListener{

            }
            btRegister.setOnClickListener{
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }
}