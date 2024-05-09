package com.example.cariaid.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cariaid.data.dataStore.DataStoreImpl
import com.example.cariaid.ui.auth.AuthActivity
import com.example.cariaid.ui.dashboard.DashBoardActivity
import com.example.cariaid.ui.onboarding.OnBoardingActivity
import com.example.cariaid.utils.Constant.USER_ONBOARDING
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
 @Inject
 lateinit var dataStoreImpl: DataStoreImpl
 private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        navigate()


    }

    private fun navigate(){
        val currentUser = auth.currentUser
        lifecycleScope.launch{
            val isDataStoreEmpty = dataStoreImpl.getValue(USER_ONBOARDING)
            if(isDataStoreEmpty.isNullOrEmpty()){
                startActivity(Intent(applicationContext,OnBoardingActivity::class.java))
            }else{
                if (currentUser!=null){
                    startActivity(Intent(applicationContext,DashBoardActivity::class.java))
                    return@launch }
                  startActivity(Intent(applicationContext,AuthActivity::class.java))
            }
        }
    }

}