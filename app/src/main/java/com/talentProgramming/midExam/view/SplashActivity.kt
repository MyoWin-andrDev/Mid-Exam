package com.talentProgramming.midExam.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.talentProgramming.midExam.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private lateinit var sharedPreferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MY_PREF", MODE_PRIVATE )
        CoroutineScope(Dispatchers.Main).launch {
            val isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false)
            delay(3000)
            if(!isUserLoggedIn){
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            else {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            }
            finish()
        }
    }
}