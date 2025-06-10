package com.talentProgramming.midExam.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.shashank.sony.fancytoastlib.FancyToast
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.adapter.StatusAdapter
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityHomeBinding
import com.talentProgramming.midExam.databinding.DialogEditStatusBinding
import com.talentProgramming.midExam.model.StatusModel
import com.talentProgramming.midExam.utilities.INTENT_USERNAME
import com.talentProgramming.midExam.utilities.KEY_USER_ID
import com.talentProgramming.midExam.utilities.SHARED_PREF_NAME
import com.talentProgramming.midExam.utilities.showAlertDialog
import com.talentProgramming.midExam.utilities.showToast

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userDb: UserDB
    private lateinit var sharedPreferences: SharedPreferences
    private var username : String? = null
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()
        setupToolbar()
        setupClickListeners()
    }

    private fun initializeComponents() {
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        userId = sharedPreferences.getInt(KEY_USER_ID, -1)
        username = intent.getStringExtra(INTENT_USERNAME)
        if (userId == -1) {
            showToast("User not found", toastType = FancyToast.ERROR)
            finish()
            return
        }

        userDb = UserDB(this)
        refreshAdapter(userDb.getUserUploadStatus(userId))
    }

    private fun setupToolbar() = with(binding.tbHome) {
        title = "Home"
        setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
                    true
                }
                R.id.logout -> {
                    showAlertDialog(
                        title = "Logout",
                        message = "Are you sure to logout?",
                        positiveButtonText = "Logout",
                        negativeButtonText = "Cancel",
                        onPositiveClick = { logout()},
                    )
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
    }

    private fun logout() {
        sharedPreferences.edit {
            clear()
            apply()
        }
        navigateToLogin()
    }



    private fun setupClickListeners() = with(binding) {
        btUpload.setOnClickListener {
            val statusText = etStatus.text.toString()
            if (statusText.isNotEmpty()) {
                if (userDb.insertStatus(userId, username?: "", statusText)) {
                    etStatus.setText("")
                    showToast("Successfully Uploaded")
                    refreshAdapter(userDb.getUserUploadStatus(userId))
                }
            } else {
                showToast("Please enter status text", FancyToast.WARNING)
            }
        }
    }

    private fun refreshAdapter(statusList: List<StatusModel>) {
        binding.rvStatus.adapter = StatusAdapter(statusList) { statusId, action ->
            when (action) {
                Action.EDIT -> showEditStatusDialog(statusId)
                Action.DELETE -> showDeleteStatusConfirmation(statusId)
            }
        }
    }

    private fun showEditStatusDialog(statusId: Int) {
        val dialogBinding = DialogEditStatusBinding.inflate(LayoutInflater.from(this), null, false)
        val status = userDb.getStatusById(statusId)

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()
            .apply {
                show()
                dialogBinding.etStatus.setText(status)

                dialogBinding.btUpdate.setOnClickListener {
                    val newStatus = dialogBinding.etStatus.text.toString()
                    if (userDb.updateStatus(newStatus, statusId)) {
                        refreshAdapter(userDb.getUserUploadStatus(userId))
                        showToast("Status updated successfully")
                        dismiss()
                    }
                }

                dialogBinding.btCancel.setOnClickListener { dismiss() }
            }
    }

    private fun showDeleteStatusConfirmation(statusId: Int) {
        showAlertDialog(
            title = "Delete Status?",
            message = "Are you sure you want to delete this status?",
            positiveButtonText = "Delete",
            negativeButtonText = "Cancel",
            onPositiveClick = {
                if (userDb.deleteStatus(statusId)) {
                    refreshAdapter(userDb.getUserUploadStatus(userId))
                    showToast("Status deleted successfully")
                }
            }
        )
    }

    sealed class Action {
        object EDIT : Action()
        object DELETE : Action()
    }
}