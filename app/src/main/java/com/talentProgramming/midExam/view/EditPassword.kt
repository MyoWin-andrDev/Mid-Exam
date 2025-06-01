package com.talentProgramming.midExam.view

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.shashank.sony.fancytoastlib.FancyToast
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityEditPasswordBinding
import com.talentProgramming.midExam.utilities.checkPassword
import com.talentProgramming.midExam.utilities.checkRePassword
import com.talentProgramming.midExam.utilities.confirmPassword
import com.talentProgramming.midExam.utilities.showToast

class EditPassword : AppCompatActivity() {

    private lateinit var binding: ActivityEditPasswordBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userDb: UserDB
    private lateinit var userName: String

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObjects()
        setupToolbar()
    }

    private fun initObjects() {
        sharedPreferences = getSharedPreferences("MY_PREF", MODE_PRIVATE)
        userDb = UserDB(this)
        userName = sharedPreferences.getString("usernameLoggedIn", null) ?: ""

    }

    private fun setupToolbar() = with(binding.tbEditPassword.tbEdit) {
        title = "Edit Password"
        setNavigationOnClickListener { finish() }
        setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.ic_save) {
                handleSaveClick()
            }
            true
        }
    }

    private fun handleSaveClick() = with(binding) {
        val newPassword = etPassword.text.toString()
        val confirmPassword = etRePassword.text.toString()

        val isPasswordValid = ilPassword.checkPassword(newPassword)
        val isRePasswordValid =
            ilRePassword.checkRePassword(newPassword, confirmPassword)

        if (isPasswordValid && isRePasswordValid) {
            confirmPassword(userName) {
                updateUserPassword(newPassword)
            }
        }
    }

    private fun updateUserPassword(password: String) {
        val userId = sharedPreferences.getInt("userId", 0)
        val success = userDb.updatePassword(userId, password)

        if (success) {
            showToast("Password Updated Successfully")
            finish()
        } else {
            showToast("Something Went Wrong.", FancyToast.ERROR)
        }
    }
}