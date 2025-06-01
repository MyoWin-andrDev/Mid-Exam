package com.talentProgramming.midExam.view

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.shashank.sony.fancytoastlib.FancyToast
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityEditUsernameBinding
import com.talentProgramming.midExam.utilities.checkUsername
import com.talentProgramming.midExam.utilities.confirmPassword
import com.talentProgramming.midExam.utilities.showToast

class EditUsername : AppCompatActivity() {

    private lateinit var binding: ActivityEditUsernameBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userDb: UserDB
    private lateinit var userName: String

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObjects()
        setData()
        setupToolbar()
    }

    private fun initObjects() {
        sharedPreferences = getSharedPreferences("MY_PREF", MODE_PRIVATE)
        userDb = UserDB(this)
    }

    private fun setData() {
        userName = sharedPreferences.getString("usernameLoggedIn", null) ?: ""
        binding.etUsername.setText(userName)
    }

    private fun setupToolbar() = with(binding.tbEditUsername.tbEdit) {
        title = "Edit Username"
        setNavigationOnClickListener { finish() }
        setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.ic_save) {
                handleSaveClick()
            }
            true
        }
    }

    private fun handleSaveClick() = with(binding) {
        val newUsername = etUsername.text.toString()

        if (ilUsername.checkUsername(this@EditUsername, newUsername, userName)) {
            confirmPassword(userName) {
                updateUserUsername(newUsername)
            }
        }
    }

    private fun updateUserUsername(newUsername: String) {
        val userId = sharedPreferences.getInt("userId", 0)
        val success = userDb.updateUser(userId, newUsername)

        if (success) {
            sharedPreferences.edit {
                putString("usernameLoggedIn", newUsername)
                apply()
            }
            showToast("Username Updated Successfully")
            finish()
        } else {
            showToast("Something went wrong.", FancyToast.ERROR)
        }
    }

}
