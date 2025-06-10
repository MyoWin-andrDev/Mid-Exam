package com.talentProgramming.midExam.view

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.shashank.sony.fancytoastlib.FancyToast
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityEditUsernameBinding
import com.talentProgramming.midExam.model.UserModel
import com.talentProgramming.midExam.utilities.KEY_USER_ID
import com.talentProgramming.midExam.utilities.SHARED_PREF_NAME
import com.talentProgramming.midExam.utilities.checkUsername
import com.talentProgramming.midExam.utilities.confirmPassword
import com.talentProgramming.midExam.utilities.showToast


class EditUsername : AppCompatActivity() {
    private lateinit var binding: ActivityEditUsernameBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userDb: UserDB
    private var userId : Int = -1
    private lateinit var user : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()
        setupToolbar()
    }


    private fun initializeViews() {
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        userDb = UserDB(this)
        userId = sharedPreferences.getInt(KEY_USER_ID, -1)
        user = userDb.getUserById(userId)!!
        binding.etUsername.setText(user.userName)
    }

    private fun setupToolbar() {
        binding.tbEditUsername.tbEdit.apply {
            title = "Edit Username"
            setNavigationOnClickListener { finish() }
            setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.ic_save) {
                    handleSaveClick()
                }
                true
            }
        }
    }

    private fun handleSaveClick() {
        val newUsername = binding.etUsername.text.toString()

        if (!isUsernameValid(newUsername)) {
            return
        }

        userId?.let { id ->
            confirmPassword(
                id,
                onSaveClick = { updateUsername(id, newUsername) }
            )
        } ?: showToast("User not found", toastType = FancyToast.ERROR)
    }

    private fun isUsernameValid(newUsername: String): Boolean {
        return binding.ilUsername.checkUsername(
            this,
            newUsername,
            user.userName
        )
    }

    private fun updateUsername(userId : Int , newUsername: String) {
        when {
            userId == -1 -> showToast("User not found", FancyToast.ERROR)
            userDb.updateUser(userId, newUsername) && userDb.updateUser(userId, newUsername) -> {
                updateSharedPreferences(newUsername)
                showToast("Username Updated Successfully")
                finish()
            }
            else -> showToast("Something went wrong!", toastType = FancyToast.ERROR)
        }
    }

    private fun updateSharedPreferences(newUsername: String) {
        sharedPreferences.edit {
            putString(SHARED_PREF_NAME, newUsername)
            apply()
        }
    }

}