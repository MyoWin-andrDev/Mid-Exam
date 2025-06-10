package com.talentProgramming.midExam.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.talentProgramming.midExam.databinding.ActivitySplashBinding
import com.talentProgramming.midExam.utilities.KEY_IS_USER_LOGGED_IN
import com.talentProgramming.midExam.utilities.SHARED_PREF_NAME
import com.talentProgramming.midExam.utilities.SPLASH_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private lateinit var sharedPreferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE)
        navigateAfterDelay()
    }
    private fun navigateAfterDelay(){
        lifecycleScope.launch {
            delay(SPLASH_DELAY)
            val nextActivity = if(isUserLoggedIn()) HomeActivity::class.java else LoginActivity::class.java
            startActivity(Intent(this@SplashActivity, nextActivity))
            finish()
        }
    }

    private fun isUserLoggedIn() : Boolean =
        sharedPreferences.getBoolean(KEY_IS_USER_LOGGED_IN, false)
}