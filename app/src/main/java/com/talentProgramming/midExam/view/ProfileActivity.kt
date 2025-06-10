package com.talentProgramming.midExam.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityProfileBinding
import com.talentProgramming.midExam.model.UserModel
import com.talentProgramming.midExam.utilities.KEY_USER_ID
import com.talentProgramming.midExam.utilities.SHARED_PREF_NAME
import com.talentProgramming.midExam.utilities.confirmPassword
import com.talentProgramming.midExam.utilities.showAlertDialog
import com.talentProgramming.midExam.utilities.showToast

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userDb: UserDB
    private lateinit var password: String
    private var userId : Int = -1
    private var user : UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeViews()
        setupToolbar()
        setupClickListeners()
    }

    private fun initializeViews() {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        userDb = UserDB(this)
        userId = sharedPreferences.getInt(KEY_USER_ID, -1)
        user = userDb.getUserById(userId)!!

    }

    private fun setupToolbar() {
        binding.tbProfile.apply {
            setSupportActionBar(this)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            btEditUsername.setOnClickListener { navigateToEditUsername() }
            btEditPassword.setOnClickListener { navigateToEditPassword() }
            btDeleteAccount.setOnClickListener { showDeleteAccountConfirmation() }
        }
    }

    private fun navigateToEditUsername() {
        startActivity(Intent(this, EditUsername::class.java))
    }

    private fun navigateToEditPassword() {
        startActivity(Intent(this, EditPassword::class.java))
    }

    private fun showDeleteAccountConfirmation() {
        if (user!!.userName.isEmpty()) {
            showToast("User not found")
            return
        }

        showAlertDialog(
            title = "Delete Account?",
            message = "Are you sure you want to delete your account?",
            positiveButtonText = "Delete",
            negativeButtonText = "Cancel",
            onPositiveClick = {
                confirmPassword(userId,
                    onSaveClick = ::deleteUserAccount,
                )
            }
        )
    }

    private fun deleteUserAccount() {
        when {
            userId == -1 -> showToast("User not found")
            userDb.deleteUser(userId) -> {
                showToast("Account deleted successfully")
                navigateToLogin()
            }
            else -> showToast("Something went wrong")
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
        finish()
    }
}