package com.talentProgramming.midExam.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.talentProgramming.midExam.databinding.ActivityProfileBinding
import com.talentProgramming.midExam.utilities.confirmPassword
import com.talentProgramming.midExam.utilities.showAlertDialog
import com.talentProgramming.midExam.utilities.showToast

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
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
//                        confirmPassword { showToast("Account Deleted Successfully") }
                    }
                )
            }
        }
    }
}