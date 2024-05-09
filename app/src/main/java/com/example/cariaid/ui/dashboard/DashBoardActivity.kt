package com.example.cariaid.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cariaid.R
import com.example.cariaid.data.model.CharityData
import com.example.cariaid.databinding.ActivityDashBoardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardActivity : AppCompatActivity() {
    lateinit var binding:ActivityDashBoardBinding

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUI()
        database = Firebase.database.reference
//        val key = database.child("charity").push().key
//           val d= CharityData(
//                charityIcon= "https://firebasestorage.googleapis.com/v0/b/cariad-adb1f.appspot.com/o/charity%2Ficons%2FcharityWater.jpeg?alt=media&token=28ab9f94-3a58-4eab-a2c4-44a83ab55940",
//                charityName= "CharityWater",
//                charityType= "Humanitarian Charity",
//                createdOn= "16/4/2022 03:13:21",
//                donationAmount= "£3000",
//                donationDesc= "This Donation is for children in rural area around the EU who don't access to water",
//                donationHeading= "Clean Water for refugee Camp",
//                donationRaised= "£0",
//                imageUrl= "https://firebasestorage.googleapis.com/v0/b/cariad-adb1f.appspot.com/o/charity%2Fcharity-water-social-preview.jpeg?alt=media&token=ab952a26-7c3e-4fde-a752-ab75f0ffefa6",
//                organisedBy= "CharityWater",
//                key = key!!
//            )
//        database.child("charity").child(key).setValue(d)
    }

    private fun setUpUI() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener{controller, destination, arguments ->
            when (destination.id) {
                R.id.navigation_home -> showBottomNavigation()
                R.id.navigation_donation -> showBottomNavigation()
                R.id.navigation_profile -> showBottomNavigation()
                R.id.navigation_history -> showBottomNavigation()
//                R.id.navigation_more -> showBottomNavigation()
                else -> hideBottomNavigation()
            }
        }
    }


    private fun hideBottomNavigation() {
        with(binding.bottomNavigationView) {
            if (visibility == VISIBLE && alpha == 1f) {
                animate()
                    .alpha(0f)
                    .withEndAction { visibility = View.GONE }
                    .duration = 100
            }
        }
    }

    // Animate timing and visibility for bottom-sheet
    private fun showBottomNavigation() {
        with(binding.bottomNavigationView) {
            visibility = VISIBLE
            animate()
                .alpha(1f)
                .duration = 100
        }
    }
}