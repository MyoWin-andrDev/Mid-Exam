package com.talentProgramming.midExam.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shashank.sony.fancytoastlib.FancyToast
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityProfileBinding
import com.talentProgramming.midExam.utilities.confirmPassword
import com.talentProgramming.midExam.utilities.showAlertDialog
import com.talentProgramming.midExam.utilities.showToast

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userDb: UserDB

    companion object {
        private const val PREF_NAME = "MY_PREF"
        private const val KEY_USERNAME = "usernameLoggedIn"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        initData()
        setupListeners()
    }

    private fun setupViews() = with(binding) {
        setSupportActionBar(tbProfile)
        tbProfile.setNavigationOnClickListener { finish() }
    }

    private fun initData() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        userDb = UserDB(this)
    }

    private fun setupListeners() = with(binding) {
        btEditUsername.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, EditUsername::class.java))
        }

        btEditPassword.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, EditPassword::class.java))
        }

        btDeleteAccount.setOnClickListener {
            handleAccountDeletion()
        }
    }

    private fun handleAccountDeletion() {
        val username = sharedPreferences.getString(KEY_USERNAME, null)

        if (username.isNullOrEmpty()) {
            showToast("User not found", FancyToast.ERROR)
            return
        }

        showAlertDialog(
            title = "Delete Account?",
            message = "Are you sure you want to delete this account?",
            positiveButtonText = "Delete",
            negativeButtonText = "Cancel",
            onPositiveClick = {
                confirmPassword(username) {
                    deleteUser(username)
                }
            }
        )
    }

    private fun deleteUser(username: String) {
        val userId = sharedPreferences.getInt("userId", 0)
        val success = userDb.deleteUser(userId)

        if (success) {
            showToast("Account Deleted Successfully")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            showToast("Something Went Wrong", FancyToast.ERROR)
        }
    }
}

