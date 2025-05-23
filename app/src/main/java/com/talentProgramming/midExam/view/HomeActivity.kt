package com.talentProgramming.midExam.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.adapter.StatusAdapter
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityHomeBinding
import com.talentProgramming.midExam.model.StatusModel
import com.talentProgramming.midExam.utilities.showAlertDialog
import com.talentProgramming.midExam.utilities.showToast

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var statusDB : UserDB
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var username : String

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("UseSupportActionBar")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)
            setSupportActionBar(tbHome)
            sharedPreferences = getSharedPreferences("MY_PREF", MODE_PRIVATE)
            //Value from Login Activity
            username = sharedPreferences.getString("usernameLoggedIn" , null)!!
            val userId = UserDB(this@HomeActivity).getUserId(username)
            showToast("Welcome $username !!!")
            statusDB  = UserDB(this@HomeActivity)
            refreshAdapter(statusDB.getUserUploadStatus(username))
            //BtnUpload
            btUpload.setOnClickListener{
                statusDB.insertStatus(userId, username, etStatus.text.toString())
                etStatus.setText("")
                showToast("Successfully Uploaded")
                refreshAdapter(statusDB.getUserUploadStatus(username))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onResume() {
        super.onResume()
        refreshAdapter(statusDB.getUserUploadStatus(username))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile -> Intent(this@HomeActivity, ProfileActivity::class.java).apply { startActivity(this) }
            R.id.logout -> {
                showAlertDialog(
                    title = "Log out",
                    message = "Are you sure you want to log out?",
                    positiveButtonText = "YES",
                    negativeButtonText = "NO",
                    onPositiveClick = {
                        //Edit PREF
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isUserLoggedIn", false)
                        editor.apply()
                        Intent(this@HomeActivity, LoginActivity::class.java).apply {
                            startActivity(this)
                        }
                        finish()
                    }
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun refreshAdapter(statusList: List<StatusModel>){
        binding.rvStatus.adapter = StatusAdapter(this@HomeActivity, username , statusList)
    }

}