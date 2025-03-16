package com.talentProgramming.midExam.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityRegisterBinding
import com.talentProgramming.midExam.utilities.showToast

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var userDB : UserDB
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        userDB = UserDB(this)
        binding.apply {
        setContentView(root)
        btLogin.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
        btSignUp.setOnClickListener{
            if(!etUsername.text.toString().equals("") && etPassword.text.toString() == etRePassword.text.toString() ){
                if(userDB.insertUser(etUsername.text.toString(),etPassword.text.toString())) {
                    showToast("Successfully Registered")
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                }
            }
            else {
                showToast("Invalid Username or Password")
            }
            }
        }
    }
}