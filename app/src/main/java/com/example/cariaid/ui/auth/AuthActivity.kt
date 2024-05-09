package com.example.cariaid.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cariaid.R
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}