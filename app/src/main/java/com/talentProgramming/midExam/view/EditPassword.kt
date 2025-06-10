package com.talentProgramming.midExam.view

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shashank.sony.fancytoastlib.FancyToast
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityEditPasswordBinding
import com.talentProgramming.midExam.model.UserModel
import com.talentProgramming.midExam.utilities.KEY_USER_ID
import com.talentProgramming.midExam.utilities.SHARED_PREF_NAME
import com.talentProgramming.midExam.utilities.checkPassword
import com.talentProgramming.midExam.utilities.checkRePassword
import com.talentProgramming.midExam.utilities.confirmPassword
import com.talentProgramming.midExam.utilities.showToast

class EditPassword : AppCompatActivity() {
    private lateinit var binding: ActivityEditPasswordBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userDb: UserDB
    private var userId : Int = -1
    private var user : UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()
        setupToolbar()
    }


    private fun initializeViews() {
        userDb = UserDB(this)
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        userId = sharedPreferences.getInt(KEY_USER_ID, -1)
        user = if(userId != -1) userDb.getUserById(userId)!! else null

    }

    private fun setupToolbar() {
        binding.tbEditPassword.tbEdit.apply {
            title = "Edit Password"
            setNavigationOnClickListener { finish() }
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.ic_save -> handleSaveClick()
                }
                true
            }
        }
    }

    private fun handleSaveClick() {
        val newPassword = binding.etPassword.text.toString()
        val confirmPassword = binding.etRePassword.text.toString()

        if (!arePasswordsValid(newPassword, confirmPassword)) {
            return
        }

        userId?.let{ id ->
            confirmPassword(
                id,
                onSaveClick = { updatePassword(id, newPassword) }
            )
        } ?: showToast("User not found", toastType = FancyToast.ERROR)
    }

    private fun arePasswordsValid(password: String, rePassword: String): Boolean {
        return binding.ilPassword.checkPassword(this, password) &&
                binding.ilRePassword.checkRePassword(this, password, rePassword)
    }

    private fun updatePassword(userId : Int, newPassword: String) {
        when {
            userId == -1 -> showToast("User not found", toastType = FancyToast.ERROR)
            userDb.updatePassword(userId, newPassword) -> {
                showToast("Password Updated Successfully")
                finish()
            }
            else -> showToast("Something went wrong")
        }
    }

}