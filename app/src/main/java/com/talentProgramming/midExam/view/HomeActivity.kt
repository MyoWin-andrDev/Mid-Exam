package com.talentProgramming.midExam.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.databinding.ActivityHomeBinding
import com.talentProgramming.midExam.utilities.showToast

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
            val username = intent.getStringExtra("username")
            Log.d("userName", username.toString())
            showToast("Hello $username !!!")
    }
}