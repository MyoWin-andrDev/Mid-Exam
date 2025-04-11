package com.talentProgramming.midExam.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityEditUsernameBinding
import com.talentProgramming.midExam.utilities.checkUsername
import com.talentProgramming.midExam.utilities.confirmPassword
import com.talentProgramming.midExam.utilities.showToast

class EditUsername : AppCompatActivity() {
    private lateinit var binding : ActivityEditUsernameBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userDb : UserDB
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUsernameBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("MY_PREF", MODE_PRIVATE)
        val username = sharedPreferences.getString("usernameLoggedIn", null)
        userDb = UserDB(this)
        binding.apply {
            setContentView(root)
            binding.tbEditUsername.tbEdit.apply {
                etUsername.setText(username)
                tbEditUsername.tbEdit.title = "Edit Username"
                tbEditUsername.tbEdit.setNavigationOnClickListener { finish() }
                tbEditUsername.tbEdit.setOnMenuItemClickListener{ item->
                    when(item.itemId){
                        R.id.ic_save -> if(ilUsername.checkUsername(this@EditUsername, etUsername.text.toString(), username)){
                            confirmPassword( username.toString(),
                                onSaveClick = {
                                    if(userDb.updateUser(userDb.getUserId(username.toString()), etUsername.text.toString())){
                                        showToast("Username Updated Successfully")
                                        sharedPreferences.edit().apply{
                                            putString("usernameLoggedIn", etUsername.text.toString())
                                            apply()
                                        }
                                        Intent(this@EditUsername, HomeActivity::class.java).apply {
                                            startActivity(this)
                                            finish()
                                        }

                                    }
                                    else{
                                        showToast("Something went wrong.")
                                    }
                                }
                            )
                        }
                    }
                    true
                }
            }
        }
    }
}