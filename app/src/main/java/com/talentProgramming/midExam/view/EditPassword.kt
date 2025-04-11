package com.talentProgramming.midExam.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.database.UserDB
import com.talentProgramming.midExam.databinding.ActivityEditPasswordBinding
import com.talentProgramming.midExam.utilities.checkPassword
import com.talentProgramming.midExam.utilities.checkRePassword
import com.talentProgramming.midExam.utilities.confirmPassword
import com.talentProgramming.midExam.utilities.showToast

class EditPassword : AppCompatActivity() {
    private lateinit var binding : ActivityEditPasswordBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userDb : UserDB
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("MY_PREF", MODE_PRIVATE)
        val username = sharedPreferences.getString("usernameLoggedIn", null)
        userDb = UserDB(this@EditPassword)
        binding.apply {
            setContentView(root)
            tbEditPassword.tbEdit.apply {
                title = "Edit Password"
                setNavigationOnClickListener { finish() }
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.ic_save ->
                            if(ilPassword.checkPassword(this@EditPassword, etPassword.text.toString()) &&
                            ilRePassword.checkRePassword(this@EditPassword, etPassword.text.toString(), etRePassword.text.toString())){
                                confirmPassword(username!!.toString(),
                                    onSaveClick = {
                                        if(userDb.updatePassword(userDb.getUserId(username), etPassword.text.toString())){
                                            showToast("Password Updated Successfully")
                                            finish()
                                        }
                                        else{
                                            showToast("Something Went Wrong.")
                                        }
                                    })
                            }
                    }
                    true
                }
            }

        }
    }
}