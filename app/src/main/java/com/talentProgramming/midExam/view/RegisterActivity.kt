package com.talentProgramming.midExam.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityRegisterBinding
import com.talentProgramming.midExam.utilities.checkPassword
import com.talentProgramming.midExam.utilities.checkRePassword
import com.talentProgramming.midExam.utilities.checkUsername
import com.talentProgramming.midExam.utilities.customStringBuilder
import com.talentProgramming.midExam.utilities.showToast

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var userDB : UserDB
    private var checkUsername = false
    private var checkPassword= false
    private var checkRePassword = false
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        userDB = UserDB(this)
        binding.apply {
            setContentView(root)
            btLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }

            btSignUp.setOnClickListener {
                //Username TextInput Validation Check
                checkUsername = ilUsername.checkUsername(this@RegisterActivity, etUsername.text.toString())
                //Password TextInput Validation Check
                checkPassword = ilPassword.checkPassword(this@RegisterActivity, etPassword.text.toString())
                //Re-Password TextInput Validation Check
                checkRePassword = ilRePassword.checkRePassword(this@RegisterActivity, etPassword.text.toString(), etRePassword.text.toString())
                //Register
                if(checkUsername && checkPassword && checkRePassword){
                    if(userDB.insertUser(etUsername.text.toString().trim(), etPassword.text.toString().trim())){
                        showToast("Your account has been successfully created.")
                        Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                            startActivity(this)
                            finish()
                        }
                    }
                }
                else{
                    showToast("Something Went Wrong. Please Try Again.")
                }
            }
        }
    }
}