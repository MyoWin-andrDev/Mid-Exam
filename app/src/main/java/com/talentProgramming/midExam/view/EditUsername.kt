package com.talentProgramming.midExam.view

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.databinding.ActivityEditUsernameBinding
import com.talentProgramming.midExam.utilities.confirmPassword
import com.talentProgramming.midExam.utilities.showToast

class EditUsername : AppCompatActivity() {
    private lateinit var binding : ActivityEditUsernameBinding
    private lateinit var sharedPreferences: SharedPreferences
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUsernameBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("MY_PREF", MODE_PRIVATE)
        val username = sharedPreferences.getString("usernameLoggedIn", null)
        binding.apply {
            setContentView(root)
            binding.tbEditUsername.tbEdit.apply {
                etUsername.setText(username)
                tbEditUsername.tbEdit.title = "Edit Username"
                tbEditUsername.tbEdit.setNavigationOnClickListener { finish() }
                tbEditUsername.tbEdit.setOnMenuItemClickListener{ item->
                    when(item.itemId){
                        R.id.ic_save -> confirmPassword(
                            onSaveClick = { showToast("Username Updated") }
                        )
                    }
                    true
                }
            }
        }
    }
}