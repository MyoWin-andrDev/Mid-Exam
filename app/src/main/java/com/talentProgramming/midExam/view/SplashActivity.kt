package com.talentProgramming.midExam.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.talentProgramming.midExam.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREF_NAME = "MY_PREF"
        private const val KEY_IS_USER_LOGGED_IN = "isUserLoggedIn"
        private const val SPLASH_DELAY = 3000L // 3 seconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        navigateAfterDelay()
    }

    private fun navigateAfterDelay() = lifecycleScope.launch {
        delay(SPLASH_DELAY)
        val isUserLoggedIn = sharedPreferences.getBoolean(KEY_IS_USER_LOGGED_IN, false)
        val nextActivity =
            if (isUserLoggedIn) HomeActivity::class.java else LoginActivity::class.java
        startActivity(Intent(this@SplashActivity, nextActivity))
        finish()
    }
}