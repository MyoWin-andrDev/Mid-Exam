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
        setContentView(binding.root)
        val username = intent.getStringExtra("username")
        val userId = intent.getIntExtra("userId", 0)
        Log.d("userName", username.toString())
//            showToast("Hello $username !!!")
        setSupportActionBar(binding.tbHome)
        statusDB  = UserDB(this@HomeActivity)
        binding.btUpload.setOnClickListener{
            statusDB.insertStatus(userId,binding.etStatus.text.toString())
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