package com.talentProgramming.midExam.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityProfileBinding
import com.talentProgramming.midExam.databinding.DialogEditAccountBinding
import com.talentProgramming.midExam.utilities.confirmPassword
import com.talentProgramming.midExam.utilities.showAlertDialog
import com.talentProgramming.midExam.utilities.showToast

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userDb : UserDB
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("MY_PREF", MODE_PRIVATE)
        val username = sharedPreferences.getString("usernameLoggedIn", null)
        userDb = UserDB(this@ProfileActivity)
        binding.apply {
            setContentView(root)
            setSupportActionBar(tbProfile)
            tbProfile.setNavigationOnClickListener { finish() }

            btEditUsername.setOnClickListener {
                Intent(this@ProfileActivity, EditUsername::class.java).apply {
                    startActivity(this)
                }
            }

            btEditPassword.setOnClickListener {
                Intent(this@ProfileActivity, EditPassword::class.java).apply {
                    startActivity(this)
                }
            }

            btDeleteAccount.setOnClickListener {
                showAlertDialog(
                    "Delete Account ?",
                    "Are you sure to delete account ?",
                    "Delete",
                    "Cancel",
                    null,
                    onPositiveClick = {
                        confirmPassword(username!!,
                            onSaveClick = {
                                if(userDb.deleteUser(userDb.getUserId(username))){
                                    showToast("Account Deleted Successfully")
                                    Intent(this@ProfileActivity, LoginActivity::class.java).apply {
                                        startActivity(this)
                                        finish()
                                    }
                                }
                                else{
                                    showToast("Something Went Wrong")
                                }
                            }
                        )
                    }
                )
            }
        }
    }
}