package com.talentProgramming.midExam.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.adapter.StatusAdapter
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityHomeBinding
import com.talentProgramming.midExam.utilities.showToast

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var statusDB : UserDB
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("UseSupportActionBar")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)
            setSupportActionBar(tbHome)

            //Value from Login Activity
            val username = intent.getStringExtra("username")
            val userId = intent.getIntExtra("userId", 0)
            val statusList = UserDB(this@HomeActivity).getUserUploadStatus(username!!)
            showToast("Welcome $username !!!")

            statusDB  = UserDB(this@HomeActivity)
            //BtnUpload
            btUpload.setOnClickListener{
                statusDB.insertStatus(userId, username, etStatus.text.toString())
            }
            rvStatus.adapter = StatusAdapter(statusList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> showToast("Successfully Logged Out !")
        }
        return super.onOptionsItemSelected(item)
    }
}