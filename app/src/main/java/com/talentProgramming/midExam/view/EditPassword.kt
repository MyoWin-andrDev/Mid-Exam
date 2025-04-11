package com.talentProgramming.midExam.view

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.talentProgramming.midExam.R
import com.talentProgramming.midExam.databinding.ActivityEditPasswordBinding
import com.talentProgramming.midExam.utilities.confirmPassword
import com.talentProgramming.midExam.utilities.showToast

class EditPassword : AppCompatActivity() {
    private lateinit var binding : ActivityEditPasswordBinding
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)
            tbEditPassword.tbEdit.apply {
                title = "Edit Password"
                setNavigationOnClickListener { finish() }
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
//                        R.id.ic_save -> confirmPassword(
//                            onSaveClick = { showToast("Password Changed Successfully") }
//                        )
                    }
                    true
                }
            }

        }
    }
}