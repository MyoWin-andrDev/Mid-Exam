package com.talentProgramming.midExam.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.databinding.ActivityProfileBinding
import com.talentProgramming.midExam.databinding.ActivitySplashBinding
import com.talentProgramming.midExam.utilities.showToast

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)
            setSupportActionBar(tbProfile)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.ic_save -> showToast("Profile Edit Successes !!!")
        }
        return super.onOptionsItemSelected(item)
    }
}