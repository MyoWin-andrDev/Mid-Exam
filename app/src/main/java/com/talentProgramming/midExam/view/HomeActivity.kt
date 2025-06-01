package com.talentProgramming.midExam.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.shashank.sony.fancytoastlib.FancyToast
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.adapter.StatusAdapter
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityHomeBinding
import com.talentProgramming.midExam.databinding.DialogEditStatusBinding
import com.talentProgramming.midExam.model.StatusModel
import com.talentProgramming.midExam.utilities.showAlertDialog
import com.talentProgramming.midExam.utilities.showToast

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userDb: UserDB
    private lateinit var sharedPreferences: SharedPreferences
    private var username: String? = null
    private var userId: Int = 0
    private lateinit var statusAdapter: StatusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObjects()
        setupCustomToolbar()
        setupWelcomeMessage()
        setupUploadButton()
        refreshAdapter()
    }

    override fun onResume() {
        super.onResume()
        refreshAdapter()
    }

    private fun initObjects() {
        sharedPreferences = getSharedPreferences("MY_PREF", MODE_PRIVATE).apply {
            userId = getInt("userId", 0)
            username = getString("usernameLoggedIn", null)
        }
        userDb = UserDB(this)
    }

    private fun setupCustomToolbar() = with(binding.tbHome) {
        title = "Home"
        setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
                    true
                }

                R.id.logout -> {
                    confirmLogout()
                    true
                }

                else -> false
            }
        }
    }


    private fun setupWelcomeMessage() {
        showToast("Welcome $username !!!")
    }

    private fun setupUploadButton() {
        binding.btUpload.setOnClickListener {
            val statusText = binding.etStatus.text.toString().trim()
            if (statusText.isEmpty()) {
                showToast("Please enter a status", FancyToast.WARNING)
                return@setOnClickListener
            }

            val isInserted = userDb.insertStatus(userId, username ?: "", statusText)
            if (isInserted) {
                binding.etStatus.text?.clear()
                showToast("Successfully Uploaded")
                refreshAdapter()
            } else {
                showToast("Upload failed. Please try again.", FancyToast.ERROR)
            }
        }
    }

    private fun refreshAdapter() {
        val statusList = userDb.getUserUploadStatus(userId)
        statusAdapter = StatusAdapter(statusList, ::onStatusItemMoreClicked)
        binding.rvStatus.adapter = statusAdapter
    }

    private fun onStatusItemMoreClicked(anchorView: View, statusItem: StatusModel) {
        PopupMenu(this, anchorView).apply {
            menuInflater.inflate(R.menu.menu_item_status, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.ic_edit -> showEditDialog(statusItem)
                    R.id.ic_delete -> showDeleteDialog(statusItem)
                }
                true
            }
            show()
        }
    }


    private fun confirmLogout() {
        showAlertDialog(
            title = "Log out",
            message = "Are you sure you want to log out?",
            positiveButtonText = "YES",
            negativeButtonText = "NO",
            onPositiveClick = {
                sharedPreferences.edit()
                    .putBoolean("isUserLoggedIn", false)
                    .apply()
                navigateToLogin()
            }
        )
    }


    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showEditDialog(statusItem: StatusModel) {
        val dialogBinding = DialogEditStatusBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()
        dialog.show()

        dialogBinding.apply {
            etStatus.setText(statusItem.status)

            btUpdate.setOnClickListener {
                val updatedStatus = etStatus.text.toString()
                if (userDb.updateStatus(updatedStatus, statusItem.statusId)) {
                    refreshStatusList()
                    showToast("Update Status Successfully")
                    dialog.dismiss()
                }
            }

            btCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    private fun showDeleteDialog(statusItem: StatusModel) {
        showAlertDialog(
            title = "Delete Status?",
            message = "Are you sure you want to delete this status?",
            positiveButtonText = "Delete",
            negativeButtonText = "Cancel",
            onPositiveClick = {
                if (userDb.deleteStatus(statusItem.statusId)) {
                    refreshStatusList()
                    showToast("Delete Status Successfully")
                }
            }
        )
    }

    private fun refreshStatusList() {
        statusAdapter.updateStatusList(userDb.getUserUploadStatus(userId))
    }

}
